package com.example.sport.ui.view.components

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    shape: CornerBasedShape = MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    sidevalue: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
//            .focusable()
        ,
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
    )
}

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    editTextSttting: EditTextSttting,
    editTextPrompt: EditTextPrompt,
    editTextIcon: EditTextIcon,
    shape: CornerBasedShape = MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    sidevalue: RememberState<String>,
    onValueChange: ((String) -> Unit)? = null
) {
//    OutlinedTextField
    TextField(
        modifier = modifier
//            .focusable()
            ,
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
    )
}

data class EditTextSttting(
    val keyboardOptions: KeyboardOptions,
    val keyboardActions: KeyboardActions,
)

class EditTextSettingBox {
    companion object {
        fun Option(
            keyboardType:KeyboardType,
            keyboardActions: () -> Unit
        ) = EditTextSttting(
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onAny = {
                keyboardActions()
            })
        )

        fun NumberOption(
            keyboardActions:() -> Unit
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
        fun ContentAndContent(
            lable: @Composable () -> Unit,
            placeholder: @Composable () -> Unit
        ) = EditTextPrompt(lable = lable, placeholder = placeholder)

        fun ContentAndText(
            lable: @Composable () -> Unit,
            placeholder: String,
        ) = EditTextPrompt(lable = lable, placeholder = { Text(text = placeholder) })

        fun TextAndContent(
            lable: String,
            placeholder: @Composable () -> Unit
        ) = EditTextPrompt(lable = { Text(text = lable) }, placeholder = placeholder)

        fun TextAndText(
            lable: String,
            placeholder: String
        ) = EditTextPrompt(lable = { Text(text = lable) },placeholder = { Text(text = placeholder) })
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