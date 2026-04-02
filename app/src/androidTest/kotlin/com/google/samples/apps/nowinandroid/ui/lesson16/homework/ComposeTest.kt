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

package com.google.samples.apps.nowinandroid.ui.lesson16.homework

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.google.samples.apps.nowinandroid.core.designsystem.component.LazyListItemPositionSemantics
import com.google.samples.apps.nowinandroid.core.designsystem.component.LazyListSizeSemantics
import com.google.samples.apps.nowinandroid.core.designsystem.component.Tags
import com.google.samples.apps.nowinandroid.ui.lesson15.ConfiguredTest
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.builder.ViewBuilder
import io.github.kakaocup.compose.node.core.BaseNode
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemBuilder
import io.github.kakaocup.compose.node.element.lazylist.KLazyListItemNode
import io.github.kakaocup.compose.node.element.lazylist.KLazyListNode
import org.junit.Test


fun BaseNode<*>.createLazyList(
    viewBuilderAction: ViewBuilder.() -> Unit,
    itemTypeBuilder: KLazyListItemBuilder.() -> Unit,
) = KLazyListNode(
    viewBuilderAction = viewBuilderAction,
    itemTypeBuilder = itemTypeBuilder,
    positionMatcher = {
        SemanticsMatcher.expectValue(LazyListItemPositionSemantics, it)
    },
    lengthSemanticsPropertyKey = LazyListSizeSemantics,
)


// Класс описания элемента списка выбора топиков (LazyHorizontalGrid).
// Семантики image/plusIcon/checkedIcon/title проброшены в ForYouScreen.kt.
class TopicsItem(
    semanticNode: SemanticsNode,
    semanticsProvider: SemanticsNodeInteractionsProvider? = null,
) : KLazyListItemNode<TopicsItem>(semanticNode, semanticsProvider) {

    val image = child<KNode> { hasTestTag(Tags.IMAGE) }
    val plusIcon = child<KNode> { hasTestTag(Tags.PLUS_ICON) }
    val checkedIcon = child<KNode> { hasTestTag(Tags.CHECKED_ICON) }
    val title = child<KNode> { hasTestTag(Tags.TITLE) }
}


// Класс описания элемента новостного фида (LazyVerticalStaggeredGrid).
// тег Tags.BOOKMARK добавлен в NewsResourceCard.kt.
class NewsItem(
    semanticNode: SemanticsNode,
    semanticsProvider: SemanticsNodeInteractionsProvider? = null,
) : KLazyListItemNode<NewsItem>(semanticNode, semanticsProvider) {

    val title = child<KNode> { hasTestTag(Tags.NEWS_TITLE) }
    val metaData = child<KNode> { hasTestTag(Tags.NEWS_META_DATA) }
    val description = child<KNode> { hasTestTag(Tags.NEWS_DESCRIPTION) }
    val bookmarkButton = child<KNode> { hasTestTag(Tags.BOOKMARK) }
}


object MainScreen : ComposeScreen<MainScreen>() {

    /** Горизонтальная сетка выбора топиков — testTag "forYou:topicSelection" */
    val topicSelectionItems = createLazyList(
        viewBuilderAction = {
            hasTestTag("forYou:topicSelection")
        },
        itemTypeBuilder = {
            itemType(::TopicsItem)
        },
    )

    /** Вертикальный staggered-grid новостного фида — testTag "forYou:feed" */
    val newsFeedItems = createLazyList(
        viewBuilderAction = {
            hasTestTag("forYou:feed")
        },
        itemTypeBuilder = {
            itemType(::NewsItem)
        },
    )
}


// Тест-сценарий: клик по нулевому элементу списка выбора топиков →
// проверяем, что нулевой элемент новостного фида отображается.
@HiltAndroidTest
class LazyListHomeworkTest : ConfiguredTest() {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun clickTopicSelectionAndVerifyFeedItem() {
        run {
            step("Кликает по нулевому элементу списка выбора топиков") {
                MainScreen {
                    topicSelectionItems.childAt<TopicsItem>(0) {
                        performClick()
                    }
                }
            }

            step("Проверяет, что нулевой элемент новостного фида отображается") {
                MainScreen {
                    newsFeedItems.childAt<NewsItem>(0) {
                        bookmarkButton.assertIsDisplayed()
                        title.assertIsDisplayed()
                        metaData.assertIsDisplayed()
                        description.assertIsDisplayed()
                    }
                }
            }
        }
    }
}
