package com.iluha168.sigmaweight.logic

import com.github.shiguruikai.combinatoricskt.combinations

fun <T> Iterable<T>.combinationsAll(): Sequence<List<T>> = sequence {
    yieldAll(combinations(0))
    yieldAll(asSequence()
        .flatMapIndexed { i, _ ->
            combinations(i+1).asSequence()
        }
    )
}