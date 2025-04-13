package com.better.alarm.domain

import com.better.alarm.data.stores.RxDataStore
import io.reactivex.Observable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StreakController(
  private val streakStore: RxDataStore<Int>,
  private val lastDateStore: RxDataStore<String>,
  private val store: Store,
  private val scoreController: ScoreController
) {
  private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

  fun onDismissedWithoutSnooze() {
    val today = dateFormat.format(Date())

    if (today != lastDateStore.value) {
      lastDateStore.value = today

      val currentStreak = streakStore.value + 1
      streakStore.value = currentStreak
      store.streak.onNext(currentStreak)

      // 🎁 Milestone bonus logic
      if (currentStreak == 5) {
        scoreController.addBonus(25)
      } else if (currentStreak > 5 && currentStreak % 5 == 0) {
        scoreController.addBonus(15)
      }
    }
  }


  fun onSnoozed() {
    val today = dateFormat.format(Date())
    if (streakStore.value > 0) {
      lastDateStore.value = today
      streakStore.value = 0
      store.streak.onNext(0)
    }
  }

  fun getStreak(): Observable<Int> = streakStore.observe()
}
