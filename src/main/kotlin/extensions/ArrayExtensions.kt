package com.grosslicht.patricksbot.extensions

import java.util.concurrent.ThreadLocalRandom



fun <T> Array<T>.random() : T = this[ThreadLocalRandom.current().nextInt(this.size)]