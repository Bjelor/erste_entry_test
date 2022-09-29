package com.bjelor.erste.ui.imagegrid

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.bjelor.erste.R

@Composable
fun SearchAppBar(
    isOpen: Boolean,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
    onSearchClick: () -> Unit,
) {
    if (isOpen) {
        OpenSearchAppBar(
            searchText = searchText,
            onSearchTextChanged = onSearchTextChanged,
            onClearClick = onClearClick,
            onNavigateBack = onNavigateBack,
            onConfirm = onConfirm
        )
    } else {
        ClosedSearchAppBar(searchText, onSearchClick)
    }

}

@Composable
fun ClosedSearchAppBar(
    searchText: String,
    onSearchClick: () -> Unit,
) {
    TopAppBar(
        title = {
            if (searchText.isNotEmpty()) {
                Text(text = searchText)
            } else {
                Text(text = stringResource(id = R.string.imagegrid_search_placeholder))
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OpenSearchAppBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onClearClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onConfirm: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = onSearchTextChanged,
                placeholder = {
                    Text(text = stringResource(id = R.string.imagegrid_search_placeholder))
                },
                maxLines = 1,
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onConfirm.invoke()
                    }
                ),
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = onClearClick) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                        }
                    }
                }
            )
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}