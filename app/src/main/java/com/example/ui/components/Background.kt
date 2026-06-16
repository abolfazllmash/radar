package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GrokAmbientBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "grok_bg_anim")

    // Slow drifting cosmic nebula 1 (Cyber Purple/Violet)
    val nebula1X by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.35f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 22000
                0.15f at 0 with LinearEasing
                0.35f at 11000 with FastOutSlowInEasing
                0.15f at 22000 with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "nebula1_x"
    )
    val nebula1Y by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 18000
                0.1f at 0 with FastOutSlowInEasing
                0.3f at 9000 with FastOutSlowInEasing
                0.1f at 18000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "nebula1_y"
    )

    // Slow drifting cosmic nebula 2 (Deep Cyan Accent)
    val nebula2X by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 0.60f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 26000
                0.85f at 0 with FastOutSlowInEasing
                0.60f at 13000 with LinearEasing
                0.85f at 26000 with FastOutSlowInEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "nebula2_x"
    )
    val nebula2Y by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 20000
                0.8f at 0 with FastOutSlowInEasing
                0.65f at 10000 with FastOutSlowInEasing
                0.8f at 20000 with LinearEasing
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "nebula2_y"
    )

    // Slow pulsing breathing effect for lighting intensity
    val pulseStrength by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_strength"
    )

    // Infinite angle rotation for floating particles constellation
    val anglePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(40000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "angle_phase"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F1012), // Elegant darkest charcoal
                        Color(0xFF0A0C0E), // Slate dark
                        Color(0xFF030405)  // Near obsidian black
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. OpenAI Mint Green Nebula glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF10A37F).copy(alpha = 0.12f * pulseStrength),
                        Color(0xFF059669).copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    center = Offset(width * nebula1X, height * nebula1Y),
                    radius = width * 1.1f
                )
            )

            // 2. Cosmic Emerald Nebula glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF0D9488).copy(alpha = 0.08f * pulseStrength),
                        Color(0xFF115E59).copy(alpha = 0.03f),
                        Color.Transparent
                    ),
                    center = Offset(width * nebula2X, height * nebula2Y),
                    radius = width * 1.2f
                )
            )

            // 3. Central atmospheric teal blending orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF14B8A6).copy(alpha = 0.03f * pulseStrength),
                        Color.Transparent
                    ),
                    center = Offset(width * 0.5f, height * 0.5f),
                    radius = width * 0.85f
                )
            )

            // 4. Drawing 16 floating, pulsing high-tech digital/constellation nodes (ChatGPT theme)
            for (i in 0 until 16) {
                val seed = i * 157.3f
                val basePercX = (seed % 100) / 100f
                val basePercY = ((seed * 19) % 100) / 100f

                // Drifting mathematics
                val driftX = sin(anglePhase + i) * 16.dp.toPx()
                val driftY = cos(anglePhase + i * 1.4f) * 20.dp.toPx()

                val oscillation = sin(anglePhase * 2.5f + i) * 0.35f + 0.65f

                val posX = (width * basePercX + driftX).coerceIn(0f, width)
                val posY = (height * basePercY + driftY).coerceIn(0f, height)

                // High-tech modern neon colors corresponding to ChatGPT mint palettes
                val particleColor = when (i % 3) {
                    0 -> Color(0xFF34D399).copy(alpha = 0.35f * oscillation) // Bright mint green
                    1 -> Color(0xFF2DD4BF).copy(alpha = 0.32f * oscillation) // Teal cyan
                    else -> Color.White.copy(alpha = 0.50f * oscillation)     // Pure star white
                }

                // Node size varying slightly
                val radius = (if (i % 3 == 2) 1.5.dp.toPx() else 2.5.dp.toPx()) * oscillation

                // Base particle
                drawCircle(
                    color = particleColor,
                    radius = radius,
                    center = Offset(posX, posY)
                )

                // Soft background halo for a gorgeous glowing digital look
                if (i % 3 != 2) {
                    drawCircle(
                        color = particleColor.copy(alpha = particleColor.alpha * 0.22f),
                        radius = 10.dp.toPx() * oscillation,
                        center = Offset(posX, posY)
                    )
                }

                // Connect nearby particles with subtle web/neural lines if they are close (to enhance AI feel)
                for (j in i + 1 until 16) {
                    val otherSeed = j * 157.3f
                    val otherBasePercX = (otherSeed % 100) / 100f
                    val otherBasePercY = ((otherSeed * 19) % 100) / 100f
                    val otherDriftX = sin(anglePhase + j) * 16.dp.toPx()
                    val otherDriftY = cos(anglePhase + j * 1.4f) * 20.dp.toPx()
                    val otherX = (width * otherBasePercX + otherDriftX).coerceIn(0f, width)
                    val otherY = (height * otherBasePercY + otherDriftY).coerceIn(0f, height)

                    val dx = posX - otherX
                    val dy = posY - otherY
                    val distanceSq = dx * dx + dy * dy
                    val threshold = 90.dp.toPx()
                    val thresholdSq = threshold * threshold

                    if (distanceSq < thresholdSq && distanceSq > 10.dp.toPx() * 10.dp.toPx()) {
                        val distance = kotlin.math.sqrt(distanceSq)
                        val lineAlpha = ((1f - distance / threshold) * 0.08f) * oscillation
                        if (lineAlpha > 0f) {
                            drawLine(
                                color = Color(0xFF5EEAD4).copy(alpha = lineAlpha),
                                start = Offset(posX, posY),
                                end = Offset(otherX, otherY),
                                strokeWidth = 0.8.dp.toPx()
                            )
                        }
                    }
                }
            }
        }
    }
}
