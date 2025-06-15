package com.krzysobo.ktsob.apptpl.widgets

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.error_occurred
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText


@Composable
fun TextFieldWithErrors(
    value: String,
    modifier: Modifier = Modifier.padding(all = 10.dp),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelText: String = "",
    placeHolderText: String = "",
    isError: Boolean = false,
    errorText: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,

    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = TextFieldDefaults.TextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    onValueChanges: (String) -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,

    // put here to avoid accidental overwriting: normally you don't need that.
    label: (@Composable () -> Unit)? = { Text(labelText) },
    placeHolder: (@Composable () -> Unit)? = { Text(placeHolderText) },
) {


    var valStr = value

    TextField(
        value = valStr,
        onValueChange = { data: String ->
            valStr = data
            onValueChanges(data)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeHolder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )

    if (isError) {
        ErrorText(errorText)
    }

}

@Composable
fun TextFieldWithErrorsKeyboardSettings(
    value: String,
    modifier: Modifier = Modifier.padding(all = 10.dp),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    labelText: String = "",
    placeHolderText: String = "",
    isError: Boolean = false,
    errorText: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    //                        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    //                        keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    focusManager: FocusManager,
    keyboardActions: KeyboardActions = KeyboardActions(
        onNext = { focusManager.moveFocus(FocusDirection.Down) }
    ),
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = TextFieldDefaults.TextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    onValueChanges: (String) -> Unit = {},
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,

    // put here to avoid accidental overwriting: normally you don't need that.
    label: (@Composable () -> Unit)? = { Text(labelText) },
    placeHolder: (@Composable () -> Unit)? = { Text(placeHolderText) },
) {


    var valStr = value

    TextField(
        value = valStr,
        onValueChange = { data: String ->
            valStr = data
            onValueChanges(data)
        },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeHolder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )

    if (isError) {
        Text(
            if (errorText != "") errorText else anyResText(AnyRes(Res.string.error_occurred)),
            style = TextStyle(color = Color(255, 0, 0))
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> soboEasyCombo(
    optionsForMenu: List<T> = emptyList(),
    onClick: (Int) -> Unit = {},
    selOptionIndex: Int = 0,
    textModifier: Modifier = Modifier
) {
    var menuExpanded = remember { mutableStateOf(false) }
    var currentOptText = remember { mutableStateOf("") }
    var currentOptIndex = remember { mutableStateOf(selOptionIndex) }

    currentOptText.value = optionsForMenu[currentOptIndex.value].toString()

    ExposedDropdownMenuBox(
        expanded = menuExpanded.value,
        onExpandedChange = { menuExpanded.value = it },
        modifier = Modifier
    ) {
        TextField(
            modifier = textModifier,
            value = currentOptText.value,
            onValueChange = { },
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded.value)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )

        ExposedDropdownMenu(
            modifier = Modifier.heightIn(max = 280.dp),
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
        ) {
            optionsForMenu.forEachIndexed { optIndex, option ->
                DropdownMenuItem(
                    content = {
                        Text(
                            option.toString(),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    },
                    onClick = {
                        currentOptText.value = option.toString()
                        currentOptIndex.value = optIndex
                        menuExpanded.value = false
                        onClick(optIndex)
                    },
                    contentPadding = PaddingValues(all = 5.dp)
                )
            }
        }
    }
}
