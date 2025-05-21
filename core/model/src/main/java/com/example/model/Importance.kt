package com.example.model

enum class Importance(val rusName: String, val engName: String) {
    LOW(rusName = "\uD83D\uDE34 Неважная", engName = "low"),
    NORMAL(rusName = "\uD83D\uDE4F Обычная", engName = "basic"),
    HIGH(rusName = "❗\uFE0F Сверхважная", engName = "important")
}