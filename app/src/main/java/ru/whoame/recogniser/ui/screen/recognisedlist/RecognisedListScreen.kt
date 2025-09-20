package ru.whoame.recogniser.ui.screen.recognisedlist

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.whoame.recogniser.R
import ru.whoame.recogniser.ui.DarkLightScreenPreviews
import ru.whoame.recogniser.ui.composable.ImageWithTitleAndMessageColumn
import ru.whoame.recogniser.ui.composable.RecognisedObjectLazyColumn
import ru.whoame.recogniser.ui.composable.ScreenTopBar
import ru.whoame.recogniser.ui.composable.model.BaseUiModel
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectLoadingUiModel
import ru.whoame.recogniser.ui.composable.model.RecognisedObjectUiModel
import ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectCard
import ru.whoame.recogniser.ui.composable.recognisedobjectcard.RecognisedObjectCardState
import ru.whoame.recogniser.ui.dialog.DeleteRecognisedItemDialog
import ru.whoame.recogniser.ui.theme.RecogniserTheme

@Composable
fun RecognisedListScreen(
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  viewModel: RecognisedListViewModel = koinViewModel(),
) {
  val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()

  RecognisedListScreenStateless(
    objects = state.list,
    selectedItemId = state.selectedItemId,
    isError = state.isErrorVisible,
    onItemClick = viewModel::itemClick,
    onItemDelete = viewModel::deleteClick,
    onBackClick = onBackClick,
    modifier = modifier,
  )

  state.itemToDelete?.let { itemToDelete ->
    DeleteRecognisedItemDialog(
      item = itemToDelete,
      onDismissRequest = viewModel::dismissDeleteDialog,
    )
  }
}

@Composable
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun RecognisedListScreenStateless(
  objects: List<BaseUiModel>,
  selectedItemId: Long?,
  isError: Boolean,
  onItemClick: (Long?) -> Unit,
  onItemDelete: (Long) -> Unit,
  onBackClick: () -> Unit,
  modifier: Modifier = Modifier,
  windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) = Column(
  horizontalAlignment = Alignment.CenterHorizontally,
  modifier = modifier,
) {
  ScreenTopBar(
    title = stringResource(R.string.recognised_object_list_title),
    onBackClick = onBackClick,
    modifier = Modifier.fillMaxWidth(),
  )

  Spacer(Modifier.weight(1f))

  val widthSizeClass = windowSizeClass.widthSizeClass
  val paddingMedium = dimensionResource(R.dimen.padding_medium)

  when {
    isError -> ImageWithTitleAndMessageColumn(
      painter = rememberVectorPainter(Icons.Default.ErrorOutline),
      title = stringResource(R.string.default_error),
      message = stringResource(R.string.recognised_object_list_error_message),
      modifier = Modifier.padding(paddingMedium),
    )

    objects.isEmpty() -> ImageWithTitleAndMessageColumn(
      painter = rememberVectorPainter(Icons.AutoMirrored.Filled.ListAlt),
      title = stringResource(R.string.recognised_object_list_empty_title),
      message = stringResource(R.string.recognised_object_list_empty_message),
      modifier = Modifier.padding(paddingMedium),
    )

    widthSizeClass == WindowWidthSizeClass.Compact -> RecognisedObjectLazyColumn(
      objects = objects,
      onItemClick = onItemClick,
      onItemDelete = onItemDelete,
      selectedItemId = selectedItemId,
    )

    widthSizeClass == WindowWidthSizeClass.Medium -> RecognisedObjectLazyColumn(
      objects = objects,
      onItemClick = onItemClick,
      onItemDelete = onItemDelete,
      modifier = Modifier.widthIn(max = dimensionResource(R.dimen.recognised_object_lazy_column_medium_max_width)),
      selectedItemId = selectedItemId,
    )

    else -> ExpandedScreenContent(objects, selectedItemId, onItemClick, onItemDelete)
  }
  Spacer(Modifier.weight(1f))
}

// FixMe Due to animations, LaunchEffect or something, the preview
//       for the expanded screen is not displaying correctly
//       https://github.com/Whoame-Dev/Recognizer/issues/14
@Composable
private fun ExpandedScreenContent(
  objects: List<BaseUiModel>,
  selectedItemId: Long?,
  onItemClick: (Long?) -> Unit,
  onItemDelete: (Long) -> Unit,
  modifier: Modifier = Modifier,
) {
  // The currently selected item from the input data
  val currentSelectedItem =
    objects.firstOrNull { it.id == selectedItemId } as? RecognisedObjectUiModel

  // The state that will store the item to be displayed in the card.
  // It will only be updated when currentSelectedItem becomes non-null
  // and will remain the last non-null value during the exit animation
  var itemToAnimate by remember { mutableStateOf<RecognisedObjectUiModel?>(null) }

  // State that controls the visibility of AnimatedVisibility
  var showAnimatedCard by remember { mutableStateOf(false) }

  // An effect that reacts to a change in the currentSelectedItem
  LaunchedEffect(key1 = currentSelectedItem) {
    if (currentSelectedItem != null) {
      // If there is a selected item, update itemToAnimate and show the card
      itemToAnimate = currentSelectedItem
      showAnimatedCard = true
    } else {
      // If the element is ‘unselected’, start the disappearance animation.
      // itemToAnimate still retains the previous value.
      showAnimatedCard = false
    }
  }

  // General animation specification for all synchronized effects
  val commonSizeAnimationSpec: SpringSpec<IntSize> = remember {
    spring(stiffness = Spring.StiffnessMediumLow)
  }

  Row(
    horizontalArrangement = Arrangement.Center,
    modifier = modifier
      .fillMaxSize()
      .animateContentSize(animationSpec = commonSizeAnimationSpec),
  ) {
    val columnMaxWidth = if (showAnimatedCard) {
      R.dimen.recognised_object_lazy_column_expanded_max_width
    } else {
      R.dimen.recognised_object_lazy_column_medium_max_width
    }
    RecognisedObjectLazyColumn(
      objects = objects,
      onItemClick = onItemClick,
      onItemDelete = onItemDelete,
      modifier = Modifier.width(dimensionResource(columnMaxWidth)),
    )
    AnimatedVisibility(
      visible = showAnimatedCard,
      enter = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
      ) + expandIn(
        animationSpec = commonSizeAnimationSpec,
        expandFrom = Alignment.CenterStart,
        initialSize = { size -> IntSize(0, size.height) },
      ) + fadeIn(),
      exit = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
      ) + shrinkOut(
        animationSpec = commonSizeAnimationSpec,
        shrinkTowards = Alignment.CenterStart,
        targetSize = { size -> IntSize(0, size.height) },
      ) + fadeOut(),
      label = stringResource(R.string.slide_in_out_animation_label),
    ) {
      Row {
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        itemToAnimate?.let { model ->
          RecognisedObjectCard(
            model = model,
            state = RecognisedObjectCardState.EXPAND,
            onClick = {
              onItemClick.invoke(null)
            },
          )
        }
      }
    }
  }
}

@DarkLightScreenPreviews
@Composable
private fun LoadingPreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    RecognisedListScreenStateless(
      objects = listOf(
        RecognisedObjectLoadingUiModel(0),
        RecognisedObjectLoadingUiModel(1),
        RecognisedObjectLoadingUiModel(2),
        RecognisedObjectLoadingUiModel(3),
        RecognisedObjectLoadingUiModel(4),
      ),
      selectedItemId = null,
      isError = false,
      onItemClick = {},
      onItemDelete = {},
      onBackClick = {},
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    )
  }
}

@DarkLightScreenPreviews
@Composable
private fun WithDataPreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    RecognisedListScreenStateless(
      objects = listOf(
        RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
        RecognisedObjectUiModel(1, R.drawable.image_placeholder, "Two", "01.01.2010"),
        RecognisedObjectUiModel(2, R.drawable.image_placeholder, "Three", "01.01.2010"),
        RecognisedObjectUiModel(3, R.drawable.image_placeholder, "Four", "01.01.2010"),
        RecognisedObjectUiModel(4, R.drawable.image_placeholder, "Five", "01.01.2010"),
      ),
      selectedItemId = null,
      isError = false,
      onItemClick = {},
      onItemDelete = {},
      onBackClick = {},
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    )
  }
}

@DarkLightScreenPreviews
@Composable
private fun WithSelectedItemPreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    RecognisedListScreenStateless(
      objects = listOf(
        RecognisedObjectUiModel(0, R.drawable.image_placeholder, "One", "01.01.2010"),
        RecognisedObjectUiModel(1, R.drawable.image_placeholder, "Two", "01.01.2010"),
        RecognisedObjectUiModel(2, R.drawable.image_placeholder, "Three", "01.01.2010"),
        RecognisedObjectUiModel(3, R.drawable.image_placeholder, "Four", "01.01.2010"),
        RecognisedObjectUiModel(4, R.drawable.image_placeholder, "Five", "01.01.2010"),
      ),
      selectedItemId = 0,
      isError = false,
      onItemClick = {},
      onItemDelete = {},
      onBackClick = {},
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    )
  }
}

@DarkLightScreenPreviews
@Composable
private fun EmptyPreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    RecognisedListScreenStateless(
      objects = listOf(),
      selectedItemId = null,
      isError = false,
      onItemClick = {},
      onItemDelete = {},
      onBackClick = {},
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    )
  }
}

@DarkLightScreenPreviews
@Composable
private fun ErrorPreview() = RecogniserTheme {
  Scaffold { innerPadding ->
    RecognisedListScreenStateless(
      objects = listOf(),
      selectedItemId = null,
      isError = true,
      onItemClick = {},
      onItemDelete = {},
      onBackClick = {},
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    )
  }
}
