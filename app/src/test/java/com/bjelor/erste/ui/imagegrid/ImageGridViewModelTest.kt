package com.bjelor.erste.ui.imagegrid

import com.bjelor.erste.domain.GetImagesUseCase
import com.bjelor.erste.domain.ReloadImagesUseCase
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

internal class ImageGridViewModelTest {

    private val reloadImagesUseCase: ReloadImagesUseCase = mockk(relaxed = true)
    private val getImagesUseCase: GetImagesUseCase = mockk(relaxed = true)

    @Test
    fun whenSearchTextIsChanged_thenSearchVariableIsUpdated() {

        // given

        val viewModel = createViewModel()

        // when

        viewModel.onSearchTextChange("new value")

        // then

        Assert.assertEquals("new value", viewModel.searchText)

    }

    private fun createViewModel() = ImageGridViewModel(
        {},
        reloadImagesUseCase,
        getImagesUseCase
    )

}