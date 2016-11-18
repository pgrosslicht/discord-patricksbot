package com.grosslicht.patricksbot.models

/**
 * Created by patrickgrosslicht on 16/11/16.
 */
data class Emblem(val difficulty: Int, val text: String, val drawOthers: Int = 0, val drawOthersAsPermanent: Int = 0) {
}