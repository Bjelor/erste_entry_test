package com.bjelor.erste.ui.imagedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.bjelor.erste.domain.Image
import com.bjelor.erste.ui.theme.FlickersteTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImageDetailScreen(
    onShare: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val viewModel: ImageDetailViewModel = koinViewModel()
    val image = viewModel.image.collectAsState().value

    ImageDetail(image, onNavigateBack, onShare)
}


@Composable
fun ImageDetail(
    image: Image?,
    onNavigateBack: () -> Unit,
    onShareClick: (String) -> Unit,
) {
    FlickersteTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = image?.title ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onShareClick(image?.title ?: "") }) {
                            Icon(Icons.Filled.Share, null)
                        }
                    }
                )
            },
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                image?.let { image ->
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth(),
                        model = image.url,
                        loading = {
                            CircularProgressIndicator()
                        },
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = image.title,
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = AnnotatedString.fromHtml(
                            htmlString = image.description
                        ),
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageDetailPreview(@PreviewParameter(LoremIpsum::class) description: String) {
    FlickersteTheme {
        ImageDetail(
            Image("", "Title", description),
            {},
            {}
        )
    }
}