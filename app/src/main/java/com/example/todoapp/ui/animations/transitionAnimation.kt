package com.example.todoapp.ui.animations

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.NavBackStackEntry

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterSlideInto(duration: Int = 350) =
    slideIntoContainer(
        animationSpec = tween(duration, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitSlideOut(duration: Int = 350) =
    slideOutOfContainer(
        animationSpec = tween(duration, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.End
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterScaleIn(duration: Int = 250) =
    scaleIn(
        animationSpec = tween(duration, easing = EaseIn),
        transformOrigin = TransformOrigin(0f,0.1f)
    ) + slideIntoContainer(
        animationSpec = tween(duration, easing = EaseIn),
        towards = AnimatedContentTransitionScope.SlideDirection.Left
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitScaleOut(duration: Int = 500) =
    scaleOut(
        animationSpec = tween(duration, easing = EaseOut),
        transformOrigin = TransformOrigin(0f,0.1f),
    ) + slideOutOfContainer(
        animationSpec = tween(duration, easing = EaseOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Right
    )
