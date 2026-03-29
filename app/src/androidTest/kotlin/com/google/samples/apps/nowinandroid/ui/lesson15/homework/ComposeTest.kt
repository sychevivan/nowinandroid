/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.ui.lesson15.homework

import com.google.samples.apps.nowinandroid.ui.lesson15.ConfiguredTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test

@HiltAndroidTest
class ComposeTest : ConfiguredTest() {

    @Test
    fun testCompose() {
        run {
            step("Проверяет элементы toolbar") {
                HomeScreen {
                    titleToolbar.assertTextEquals("Now in Android")
                    iconSearchToolbar.assertIsDisplayed()
                    iconOptionsToolbar.assertIsDisplayed()
                }
            }
            step("Проверяет элементы HomeScreen") {
                HomeScreen {
                    titleHomePage {
                        assertIsDisplayed()
                        assertTextContains("What", ignoreCase = true, substring = true)
                    }
                    subTitleHomePage {
                        assertIsDisplayed()
                        assertTextContains("Updates", ignoreCase = true, substring = true)
                    }
                    doneButton.assertIsDisplayed()
                    doneButtonText.assertTextEquals("Done")
                }
            }
            step("Нажимает на кнопку iconSearchToolbar") {
                HomeScreen {
                    iconSearchToolbar.performClick()
                }
            }
            step("Проверяет элементы SearchScreen") {
                SearchScreen {
                    iconBack.assertIsDisplayed()
                    iconSearchScreen.assertIsDisplayed()
                    searchTextField.assertIsDisplayed()
                    recentSearchesText.assertTextEquals("Recent searches")
                }
            }
        }
    }
}