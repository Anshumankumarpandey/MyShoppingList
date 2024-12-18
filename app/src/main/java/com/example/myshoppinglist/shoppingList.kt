package com.example.myshoppinglist

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


data class ShoppingItem(val id:Int,
                        var name: String,
                        var quantity:Int,
                        var isEditing: Boolean =false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppListApp(){
        var sItems by remember { mutableStateOf(listOf<ShoppingItem>())}
        var showDialog by remember { mutableStateOf(false) }
        var itemName by  remember {mutableStateOf ("")}
        var itemQuantity by remember{mutableStateOf("")}

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick ={showDialog = true},
                modifier= Modifier.align(Alignment.CenterHorizontally)
            )
            {
             Text("Add Item")
            }
            LazyColumn(
                modifier= Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ){
                //this is the function made for editing part(Modify)that we perform edit on saved values
                items(sItems) { item ->
                    if (item.isEditing) {
                        ShoppingItemEditor(
                            item = item,
                            onEditComplete = { editedName, editQuantity ->
                                sItems = sItems.map {
                                    it.copy(isEditing = false)
                                }//this few lines tell how it will perform when edit  is complete
                                val editedItem =
                                    sItems.find { it.id == item.id } //this find function in this find the item we are currently editing
                                editedItem?.let { //open the currently edited item
                                    it.name =
                                        editedName //now it have the name that we enter after edit
                                    it.quantity = editQuantity
                                }
                            })

                    } else {
                        ShoppingListItem(item = item, onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = it.id==item.id) } //the shoping list element that is edited we are checking its id with-
                            //-with before edit element id if true then editing is true or other then false to check which one is edited .
                        }, onDeleteClick = {
                            sItems =sItems-item //to get rid of the item that we want to delete
                        })

                    }

                }
            }
        }

    if(showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween){
                                Button(onClick = {
                                    if(itemName.isNotBlank()){
                                        val newItem = ShoppingItem(
                                            id =sItems.size+1,
                                            name = itemName,
                                            quantity = itemQuantity.toInt()

                                            )
                                         sItems =sItems + newItem
                                         showDialog = false
                                        itemName =""
                                    }
                                }) {
                                    Text("Add")
                                }
                                Button(onClick = {showDialog=false}) {
                                Text("Cancel")
                                }

                            }


            },
            title = { Text("Add Shopping Items") },
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = { itemName = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(value = itemQuantity, onValueChange = {itemQuantity =it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        )
                }


            }

        )

    }
}
@Composable
fun ShoppingItemEditor (item: ShoppingItem, onEditComplete: (String, Int) -> Unit ){
    var editedName by remember {mutableStateOf(item.name)}
    var editQuantity by remember {mutableStateOf(item.quantity.toString())}
    var isEditing by remember{mutableStateOf(item.isEditing)}
    Row (modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement =Arrangement.SpaceEvenly )
    {
      Column {
          BasicTextField(
              value = editedName ,
              onValueChange ={editedName = it} ,//when ever we are editing text field this will make
              //on value change and keep it in (it) variable then assign it to the editedName
              singleLine = true,
              modifier = Modifier
                  .wrapContentSize()
                  .padding(8.dp)
          )
          BasicTextField(
              value =editQuantity ,
              onValueChange = {editQuantity =it},
              singleLine = true,
              modifier= Modifier
                  .wrapContentSize()
                  .padding(8.dp)

          )
      }
    Button(
        onClick = {
            isEditing =false
            onEditComplete(editedName, editQuantity.toIntOrNull()?: 1) //if you enter somthing error value it will jump on one for the quantity
        }
    ) {
        Text("Save")
    }



    }

}

@Composable
fun ShoppingListItem (
    item: ShoppingItem,
    //unit is like our own onclick function and we can assign a function on it
    onEditClick:()->Unit, //this is lamda expression this useually meanse no input and no output
    onDeleteClick:()->Unit,

    ) {

Row(
    modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            border = BorderStroke(2.dp, Color(0XFF018786)),
            shape = RoundedCornerShape(20)
        ),
    horizontalArrangement =Arrangement.SpaceBetween
){

    Text(text= item.name, Modifier.padding(8.dp))
    Text(text = "Qty : ${item.quantity}",modifier = Modifier.padding(8.dp) )
    Row(modifier = Modifier.padding(8.dp)){
        IconButton(onClick = onEditClick){
            Icon(imageVector = Icons.Default.Edit,contentDescription = null)
        }
        IconButton(onClick = onDeleteClick) {
          Icon(imageVector = Icons.Default.Delete,contentDescription = null)

        }
    }
}
 }

