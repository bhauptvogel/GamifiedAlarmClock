package com.better.alarm.domain

import com.better.alarm.data.stores.RxDataStore
import io.reactivex.Observable

class ScoreController(
    private val scoreStore: RxDataStore<Int>,
    private val store: Store
) {
    fun currentScore(): Observable<Int> = scoreStore.observe()

    fun onDismissedWithoutSnooze() {
        val current = scoreStore.value
        val reward = if (current < 50) 15 else 10
        val newScore = (current + reward).coerceAtLeast(0)
        scoreStore.value = newScore
        store.score.onNext(newScore)
    }

    fun onSnoozed(penalty: Int) {
        val current = scoreStore.value
        val newScore = (current + penalty).coerceAtLeast(0)
        scoreStore.value = newScore
        store.score.onNext(newScore)
    }

    fun addBonus(amount: Int) {
        val newScore = (scoreStore.value + amount).coerceAtLeast(0)
        scoreStore.value = newScore
        store.score.onNext(newScore)
    }
}


