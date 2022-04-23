package com.example.sport.ui.view.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.billing.ui.theme.focusedColor
import com.example.billing.ui.theme.keyboard
import com.example.billing.utils.RememberState
import com.google.accompanist.insets.LocalWindowInsets

//val focusManager = LocalFocusManager.current   获取焦点监听

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = LocalWindowInsets.current.ime.isVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    editTextSttting: EditTextSttting,
    editTextPrompt: EditTextPrompt,
    editTextIcon: EditTextIcon,
    shape: CornerBasedShape = MaterialTheme.shapes.small.copy(
        bottomEnd = ZeroCornerSize,
        bottomStart = ZeroCornerSize
    ),
    sidevalue: String,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onBackground,
        focusedLabelColor = MaterialTheme.colors.focusedColor,
        unfocusedLabelColor = MaterialTheme.colors.onBackground,
        focusedIndicatorColor = MaterialTheme.colors.focusedColor,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        cursorColor = MaterialTheme.colors.onBackground,
        leadingIconColor = MaterialTheme.colors.onBackground,
        trailingIconColor = MaterialTheme.colors.onBackground,
        placeholderColor = MaterialTheme.colors.onBackground
    ),
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
//            .focusable()
        value = sidevalue,
        onValueChange = {
            onValueChange(it)
        },
        label = editTextPrompt.lable,
        placeholder = editTextPrompt.placeholder,
        leadingIcon = editTextIcon.leadingIcon,
        trailingIcon = editTextIcon.trailingIcon,
        singleLine = true,
        shape = shape,
        keyboardOptions = editTextSttting.keyboardOptions,
        keyboardActions = editTextSttting.keyboardActions,
        colors = colors
    )
}

@Composable
fun EditTextDouble(
    modifier: Modifier = Modifier,
    editTextSttting: EditTextSttting,
    editTextPrompt: EditTextPrompt,
    editTextIcon: EditTextIcon,
    shape: CornerBasedShape = MaterialTheme.shapes.small.copy(
        bottomEnd = ZeroCornerSize,
        bottomStart = ZeroCornerSize
    ),
    sidevalue: RememberState<Double>,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onBackground,
        focusedLabelColor = MaterialTheme.colors.focusedColor,
        unfocusedLabelColor = MaterialTheme.colors.onBackground,
        focusedIndicatorColor = MaterialTheme.colors.focusedColor,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        cursorColor = MaterialTheme.colors.onBackground,
        leadingIconColor = MaterialTheme.colors.onBackground,
        trailingIconColor = MaterialTheme.colors.onBackground,
        placeholderColor = MaterialTheme.colors.onBackground
    ),
    onValueChange: ((String) -> Unit)? = null
) {
    val a = sidevalue.value
//    OutlinedTextField
    TextField(
        modifier = Modifier
//            .background(color = MaterialTheme.colors.background)
            .then(modifier),
//            .focusable()
        value = sidevalue.getState().value.toString(),
        onValueChange = {
            try {
                sidevalue set it.toDouble()
                if (onValueChange != null) {
                    onValueChange(it)
                }
                Log.w("nol",sidevalue.value.toString())

            }catch (e:NumberFormatException) {
                sidevalue set a
                Log.w("erro",sidevalue.value.toString())

            }
        },
        label = editTextPrompt.lable,
        placeholder = editTextPrompt.placeholder,
        leadingIcon = editTextIcon.leadingIcon,
        trailingIcon = editTextIcon.trailingIcon,
        singleLine = true,
        shape = shape,
        keyboardOptions = editTextSttting.keyboardOptions,
        keyboardActions = editTextSttting.keyboardActions,
        colors = colors
    )
}

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    editTextSttting: EditTextSttting,
    editTextPrompt: EditTextPrompt,
    editTextIcon: EditTextIcon,
    shape: CornerBasedShape = MaterialTheme.shapes.small.copy(
        bottomEnd = ZeroCornerSize,
        bottomStart = ZeroCornerSize
    ),
    sidevalue: RememberState<String>,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onBackground,
        focusedLabelColor = MaterialTheme.colors.focusedColor,
        unfocusedLabelColor = MaterialTheme.colors.onBackground,
        focusedIndicatorColor = MaterialTheme.colors.focusedColor,
        backgroundColor = MaterialTheme.colors.primaryVariant,
        cursorColor = MaterialTheme.colors.onBackground,
        leadingIconColor = MaterialTheme.colors.onBackground,
        trailingIconColor = MaterialTheme.colors.onBackground,
        placeholderColor = MaterialTheme.colors.onBackground
    ),
    onValueChange: ((String) -> Unit)? = null
) {
//    OutlinedTextField
    TextField(
        modifier = Modifier
//            .background(color = MaterialTheme.colors.background)
            .then(modifier),
//            .focusable()
        value = sidevalue.getState().value,
        onValueChange = {
            sidevalue set it
            if (onValueChange != null) {
                onValueChange(it)
            }
        },
        label = editTextPrompt.lable,
        placeholder = editTextPrompt.placeholder,
        leadingIcon = editTextIcon.leadingIcon,
        trailingIcon = editTextIcon.trailingIcon,
        singleLine = true,
        shape = shape,
        keyboardOptions = editTextSttting.keyboardOptions,
        keyboardActions = editTextSttting.keyboardActions,
        colors = colors
    )
}

data class EditTextSttting(
    val keyboardOptions: KeyboardOptions,
    val keyboardActions: KeyboardActions,
)

class EditTextSettingBox {
    companion object {
        fun Option(
            keyboardType: KeyboardType,
            keyboardActions: () -> Unit
        ) = EditTextSttting(
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onAny = {
                keyboardActions()
            })
        )

        fun NumberOption(
            keyboardActions: () -> Unit
        ) = Option(
            keyboardType = KeyboardType.Number,
            keyboardActions = keyboardActions
        )

        fun TextOption(
            keyboardActions: () -> Unit
        ) = Option(
            keyboardType = KeyboardType.Text,
            keyboardActions = keyboardActions
        )
    }
}

data class EditTextPrompt(
    val lable: @Composable () -> Unit,
    val placeholder: @Composable () -> Unit,
)

class EditTextPromptBox {
    companion object {
        @Composable
        fun contentAndContent(
            lable: @Composable () -> Unit,
            placeholder: @Composable () -> Unit
        ) = EditTextPrompt(lable = lable, placeholder = placeholder)

        @Composable
        fun contentAndText(
            lable: @Composable () -> Unit,
            placeholder: String,
        ) = EditTextPrompt(lable = lable, placeholder = { Text(text = placeholder) })

        @Composable
        fun textAndContent(
            lable: String,
            placeholder: @Composable () -> Unit
        ) = EditTextPrompt(lable = { Text(text = lable) }, placeholder = placeholder)

        @Composable
        fun textAndText(
            lable: String,
            placeholder: String,
        ) = EditTextPrompt(lable = { Text(text = lable) }, placeholder = { Text(text = placeholder) })
    }
}

data class EditTextIcon(
    val leadingIcon: (@Composable () -> Unit)? = null,
    val trailingIcon: (@Composable () -> Unit)? = null
)

class EditTextIconBox {
    companion object {
        fun LeftAndRight(
            leadingIcon: @Composable () -> Unit,
            trailingIcon: @Composable () -> Unit
        ) = EditTextIcon(leadingIcon = leadingIcon, trailingIcon = trailingIcon)

        fun Left(
            leadingIcon: @Composable () -> Unit
        ) = EditTextIcon(leadingIcon = leadingIcon, trailingIcon = null)

        fun Right(
            trailingIcon: @Composable () -> Unit
        ) = EditTextIcon(leadingIcon = null, trailingIcon = trailingIcon)

        fun Null() = EditTextIcon(leadingIcon = null, trailingIcon = null)
    }
}