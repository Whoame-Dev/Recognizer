package ru.whoame.recogniser.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import ru.whoame.recogniser.R
import ru.whoame.recogniser.model.RecognisedObject
import ru.whoame.recogniser.ui.DarkLightPreviews
import ru.whoame.recogniser.ui.composables.RecognisedObjectCard
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun RecognisedObjectListScreen(objects: List<RecognisedObject>) {
    var selectedItemId by rememberSaveable { mutableStateOf<Int?>(null) }

    RecognisedObjectListScreenStateless(
        objects = objects,
        selectedItemId = selectedItemId,
        onItemClick = { id ->
            selectedItemId = id
        },
    )
}

@Composable
private fun RecognisedObjectListScreenStateless(
    objects: List<RecognisedObject>,
    selectedItemId: Int?,
    onItemClick: (Int?) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val paddingSmall = dimensionResource(R.dimen.padding_small)

    Scaffold { innerPaddings ->
        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(paddingSmall),
            userScrollEnabled = selectedItemId == null,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
        ) {
            itemsIndexed(objects) { index, item ->
                val itemId = item.id
                val isSelected = selectedItemId == itemId
                RecognisedObjectCard(
                    recognisedObject = item,
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
                    modifier = Modifier.padding(bottom = paddingSmall).let { modifier ->
                        if (isSelected) {
                            modifier.fillParentMaxSize()
                        } else {
                            modifier
                        }
                    },
                )
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectListScreenPreview() = RecogniserTheme {
    AndroidThreeTen.init(LocalContext.current)
    RecognisedObjectListScreenStateless(
        objects = listOf(
            RecognisedObject(0, "One", LocalDateTime.now()),
            RecognisedObject(1, "Two", LocalDateTime.now()),
            RecognisedObject(2, "Three", LocalDateTime.now()),
            RecognisedObject(3, "Four", LocalDateTime.now()),
            RecognisedObject(4, "Five", LocalDateTime.now()),
        ),
        selectedItemId = null,
        onItemClick = {},
    )
}

@DarkLightPreviews
@Composable
private fun RecognisedObjectListScreenWithSelectedItemPreview() = RecogniserTheme {
    AndroidThreeTen.init(LocalContext.current)
    RecognisedObjectListScreenStateless(
        objects = listOf(
            RecognisedObject(0, "One", LocalDateTime.now()),
            RecognisedObject(1, "Two", LocalDateTime.now()),
            RecognisedObject(2, "Three", LocalDateTime.now()),
            RecognisedObject(3, "Four", LocalDateTime.now()),
            RecognisedObject(4, "Five", LocalDateTime.now()),
        ),
        selectedItemId = 0,
        onItemClick = {},
    )
}
