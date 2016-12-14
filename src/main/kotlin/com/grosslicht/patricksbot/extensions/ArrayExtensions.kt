package com.grosslicht.patricksbot.extensions

import java.util.concurrent.ThreadLocalRandom

/**
 * Created by patrickgrosslicht on 14/12/16.
 */

fun <T> Array<T>.random() : T = this[ThreadLocalRandom.current().nextInt(this.size)]