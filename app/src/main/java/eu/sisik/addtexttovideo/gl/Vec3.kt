package eu.sisik.addtexttovideo.gl

import kotlin.math.sqrt

/**
 * Copyright (c) 2018 by Roman Sisik. All rights reserved.
 */


data class Vec3(var x: Float = 0f,
                var y: Float = 0f,
                var z: Float = 0f) {


    operator fun plus(other: Vec3): Vec3 {
        return Vec3(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vec3): Vec3 {
        return Vec3(x - other.x, y - other.y, z - other.z)
    }

    operator fun times(vec: Vec3): Vec3 {
        return Vec3(x * vec.x, y * vec.y, z * vec.z)
    }

    operator fun times(scalar: Float): Vec3 {
        return Vec3(x * scalar, y * scalar, z * scalar)
    }

    operator fun div(scalar: Float): Vec3 {
        return Vec3(x / scalar, y / scalar, z / scalar)
    }
}

fun distance(first: Vec3, second: Vec3): Float {
    return length(first - second)
}

fun length(vec: Vec3): Float {
    return sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z)
}

fun normalize(vec: Vec3): Vec3 {
    val l = length(vec)
    if (l == 0f)
        return vec

    val lr = 1f / l
    return vec * lr
}

fun clamp(vec: Vec3, toLength: Float): Vec3 {
    val l = length(vec)
    return if (l > toLength) normalize(vec) * toLength else vec
}