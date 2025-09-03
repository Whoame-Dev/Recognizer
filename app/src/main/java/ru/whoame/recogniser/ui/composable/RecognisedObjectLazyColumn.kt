package ru.whoame.recogniser.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import kotlinx.coroutines.launch
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.composable.model.BaseUiModel
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectLoadingUiModel
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectCardShimmer
import ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectSwipeToDeleteCard
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun RecognisedObjectLazyColumn(
    objects: List<BaseUiModel>,
    onItemClick: (Long?) -> Unit,
    onItemDelete: (Long) -> Unit,
    modifier: Modifier = Modifier,
    selectedItemId: Long? = null,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val paddingSmall = dimensionResource(R.dimen.padding_small)

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(paddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        userScrollEnabled = selectedItemId == null,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = objects,
            key = { _, value -> value.id },
        ) { index, model ->
            val itemModifier = Modifier.padding(bottom = paddingSmall)
            when (model) {
                is RecognisedObjectLoadingUiModel ->
                    RecognisedObjectCardShimmer(itemModifier)

                is RecognisedObjectUiModel -> {
                    val itemId = model.id
                    val isSelected = selectedItemId == itemId
                    RecognisedObjectSwipeToDeleteCard(
                        model = model,
                        isExpanded = isSelected,
                        onClick = {
                            val newSelectedItemId = if (selectedItemId == itemId) {
                                null
                            } else {
                                coroutineScope.launch {
                                    lazyListState.animateScrollToItem(index = index)
                                }
                                itemId
                            }
                            onItemClick.invoke(newSelectedItemId)
                        },
                        onDelete = {
                            onItemDelete.invoke(itemId)
                        },
                        modifier = if (isSelected) {
                            itemModifier.fillParentMaxSize()
                        } else {
                            itemModifier
                        },
                    )
                }
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun FoldedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectLazyColumn(
            objects = listOf(
                RecognisedObjectLoadingUiModel(0),
                RecognisedObjectLoadingUiModel(1),
                RecognisedObjectUiModel(2, R.drawable.image_placeholder, "Three", "01.01.2010"),
                RecognisedObjectUiModel(3, R.drawable.image_placeholder, "Four", "01.01.2010"),
                RecognisedObjectUiModel(4, R.drawable.image_placeholder, "Five", "01.01.2010"),
            ),
            onItemClick = {},
            onItemDelete = {},
        )
    }
}

@DarkLightPreviews
@Composable
private fun ExpandedPreview() = RecogniserTheme {
    Surface {
        RecognisedObjectLazyColumn(
            objects = listOf(
                RecognisedObjectUiModel(0, R.drawable.image_placeholder, "Three", "01.01.2010"),
                RecognisedObjectLoadingUiModel(1),
                RecognisedObjectLoadingUiModel(2),
                RecognisedObjectUiModel(3, R.drawable.image_placeholder, "Four", "01.01.2010"),
                RecognisedObjectUiModel(4, R.drawable.image_placeholder, "Five", "01.01.2010"),
            ),
            onItemClick = {},
            onItemDelete = {},
            selectedItemId = 0,
        )
    }
}
