package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun RadarLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Glowing Mint-to-Emerald Gradient Container for the newly generated radar logo
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF10A37F), Color(0xFF34D399))
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        color = Color.White.copy(alpha = 0.45f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_radar_logo),
                    contentDescription = "Radar Icon",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(13.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            // Styled Heading Typography for رادار with Persian Gradient feel
            Column(horizontalAlignment = Alignment.Start) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "رادار",
                        color = Color(0xFF34D399),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Radar",
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 1.5.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(10.dp))
        
        // Beautiful minimal subtitle divider
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color(0xFF10A37F).copy(alpha = 0.6f), Color.Transparent)
                    )
                )
        )
        Spacer(modifier = Modifier.height(10.dp))
        
        Text(
            text = "مشاوره تکنولوژی و تحلیل عمیق سخت‌افزار با رادار و هوش مصنوعی",
            color = Color.White.copy(alpha = 0.65f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

