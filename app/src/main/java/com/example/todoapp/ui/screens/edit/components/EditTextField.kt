package com.example.todoapp.ui.screens.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.ui.screens.edit.action.EditUiAction
import com.example.todoapp.ui.themes.ExtendedTheme
import com.example.todoapp.ui.themes.ThemePreview
import com.example.todoapp.ui.themes.TodoAppTheme


@Composable
fun EditTextField(
    text: String,
    uiAction: (EditUiAction) -> Unit
) {
    BasicTextField(
        value = text,
        onValueChange = { uiAction(EditUiAction.UpdateText(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .background(
                color = ExtendedTheme.colors.backSecondary,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp)),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = ExtendedTheme.colors.labelPrimary
        ),
        minLines = 3,
        cursorBrush = SolidColor(ExtendedTheme.colors.labelPrimary)
    ) { textField ->
        Box(
            modifier = Modifier
                .padding(15.dp)
        ) {
            if (text.isEmpty())
                Text(
                    text = stringResource(id = R.string.edit_text_field_hint),
                    color = ExtendedTheme.colors.labelTertiary
                )
            textField.invoke()
        }

    }
}

@Preview
@Composable
fun PreviewTextField(
    @PreviewParameter(ThemePreview::class) isDarkTheme: Boolean
) {
    TodoAppTheme(isDarkTheme) {
        EditTextField(
            text = "",
            uiAction = {}
        )
    }
}