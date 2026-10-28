package com.mahalaxmi.wakedrive.utils

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioFocusRequest
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import android.util.Log
import com.mahalaxmi.wakedrive.data.models.AlertLevel
import kotlinx.coroutines.*

class SoundManager(private val context: Context) {
    companion object {
        private const val TAG = "SoundManager"
    }

    private var mediaPlayer: MediaPlayer? = null
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var _isPlaying = false
    private val isPlaying: Boolean get() = _isPlaying
    private var audioFocusRequest: AudioFocusRequest? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var currentAlertLevel: AlertLevel = AlertLevel.NORMAL

    init {
        Log.d(TAG, "Initializing SoundManager")
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
        try {
            Log.d(TAG, "Setting up MediaPlayer")
            stopSound()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                )
                setOnCompletionListener {
                    Log.d(TAG, "MediaPlayer completed playback")
                    if (!isLooping) {
                        _isPlaying = false
                        abandonAudioFocus()
                    }
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what extra=$extra")
                    _isPlaying = false
                    abandonAudioFocus()
                    scope.launch {
                        delay(1000)
                        setupMediaPlayer()
                    }
                    true
                }
                setOnPreparedListener {
                    Log.d(TAG, "MediaPlayer prepared, starting playback")
                    try {
                        if (requestAudioFocus()) {
                            start()
                            _isPlaying = true
                        } else {
                            Log.e(TAG, "Failed to get audio focus before playback")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error starting playback", e)
                        _isPlaying = false
                    }
                }
            }
            Log.d(TAG, "MediaPlayer setup completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up MediaPlayer", e)
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun requestAudioFocus(): Boolean {
        Log.d(TAG, "Requesting audio focus")
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setOnAudioFocusChangeListener { focusChange ->
                    Log.d(TAG, "Audio focus changed: $focusChange")
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS,
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                            Log.d(TAG, "Audio focus lost, stopping sound")
                            stopSound()
                        }
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                            Log.d(TAG, "Audio focus ducking")
                            mediaPlayer?.setVolume(0.3f, 0.3f)
                        }
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            Log.d(TAG, "Audio focus gained")
                            mediaPlayer?.setVolume(1.0f, 1.0f)
                            if (currentAlertLevel in listOf(AlertLevel.SEVERE, AlertLevel.CRITICAL)) {
                                playAlertSound(currentAlertLevel)
                            }
                        }
                    }
                }
                .build()
            audioManager.requestAudioFocus(audioFocusRequest!!)
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS,
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> stopSound()
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> mediaPlayer?.setVolume(0.3f, 0.3f)
                        AudioManager.AUDIOFOCUS_GAIN -> {
                            mediaPlayer?.setVolume(1.0f, 1.0f)
                            if (currentAlertLevel in listOf(AlertLevel.SEVERE, AlertLevel.CRITICAL)) {
                                playAlertSound(currentAlertLevel)
                            }
                        }
                    }
                },
                AudioManager.STREAM_ALARM,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            )
        }
        val granted = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        Log.d(TAG, "Audio focus request result: $granted")
        return granted
    }

    private fun abandonAudioFocus() {
        Log.d(TAG, "Abandoning audio focus")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let {
                audioManager.abandonAudioFocusRequest(it)
                audioFocusRequest = null
            }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(null)
        }
    }

    fun playAlertSound(alertLevel: AlertLevel) {
        Log.d(TAG, "Playing alert sound for level: $alertLevel")
        if (alertLevel == currentAlertLevel && alertLevel != AlertLevel.NORMAL) {
            Log.d(TAG, "Skipping alert sound - same level already playing")
            return
        }
        currentAlertLevel = alertLevel
        scope.launch {
            when (alertLevel) {
                AlertLevel.WARNING -> playWarningSound()
                AlertLevel.SEVERE -> playSevereSound()
                AlertLevel.CRITICAL -> playCriticalSound()
                else -> {
                    Log.d(TAG, "Normal alert level, stopping sound")
                    stopSound()
                }
            }
        }
    }

    private fun playWarningSound() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        playSound(notification, false)
        vibrateDevice(500)
    }

    private fun playSevereSound() {
        val alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        playSound(alarm, true)
        vibrateDevice(1000)
    }

    private fun playCriticalSound() {
        val alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        playSound(alarm, true)
        vibratePattern()
    }

    private fun playSound(uri: android.net.Uri, loop: Boolean) {
        try {
            stopSound()
            if (mediaPlayer == null) {
                setupMediaPlayer()
            }
            mediaPlayer?.apply {
                reset()
                setDataSource(context, uri)
                isLooping = loop
                setVolume(1.0f, 1.0f)
                prepareAsync()
            } ?: run {
                Log.e(TAG, "MediaPlayer is still null after setup attempt")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error playing sound", e)
            _isPlaying = false
            abandonAudioFocus()
            scope.launch {
                delay(1000)
                setupMediaPlayer()
            }
        }
    }

    private fun vibrateDevice(duration: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error vibrating device", e)
        }
    }

    private fun vibratePattern() {
        try {
            val pattern = longArrayOf(0, 500, 200, 500, 200, 500)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(pattern, 0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in vibration pattern", e)
        }
    }

    fun stopSound() {
        Log.d(TAG, "Stopping sound")
        try {
            mediaPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                reset()
            }
            vibrator.cancel()
            _isPlaying = false
            abandonAudioFocus()
            currentAlertLevel = AlertLevel.NORMAL
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping sound", e)
            mediaPlayer?.release()
            mediaPlayer = null
            setupMediaPlayer()
        }
    }

    fun release() {
        Log.d(TAG, "Releasing SoundManager resources")
        try {
            scope.cancel()
            stopSound()
            mediaPlayer?.release()
            mediaPlayer = null
            vibrator.cancel()
            _isPlaying = false
            abandonAudioFocus()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing SoundManager", e)
        }
    }
}
