from tkinter import *
from datetime import datetime
import tkinter as tk

root = Tk()

root.title("todo Project")
root.geometry("500x500")
root.configure(background="#94d3f7")

font = ("Courier", 15, "italic")

# creating app frame


label_Frame = Frame(root, bg="#94d3f7")
label_Frame.pack(pady=10)

name_top_label = Label(label_Frame, text="nazwa zadania")
set_date_label = Label(label_Frame, text="data utworzenia")
deadline_lael = Label(label_Frame, text="deadline")

name_top_label.grid(row=0, column=0)
set_date_label.grid(row=0, column=1,padx=60)
deadline_lael.grid(row=0, column=2)

box_frame = tk.Canvas(root, width=700, height=650)
box_frame.pack()

# creating listbox

list_box = Listbox(
    box_frame,
    font=font,
    width=50,
    height=9,
    bg="SystemButtonFace",
    fg="black",
    highlightthickness=0,
    background="white",
)

list_box.pack(side=LEFT, fill=BOTH)

tasks_list = []

# adding tasks to list_box
for task in tasks_list:
    list_box.insert(END, task)

# Create scrollbar
scrollBar = Scrollbar(box_frame, bg="#94d3f7")
scrollBar.pack(side=LEFT, fill=BOTH)

# Add scrollBar
list_box.config(yscrollcommand=scrollBar)
scrollBar.config(command=list_box.yview())

# create entry box to add item to the list
entry_frame = Frame(root, bg="#94d3f7")
entry_frame.pack(pady=30)

name_label = Label(entry_frame, text = "wprowadź nazwę")
deadline_label = Label(entry_frame, text="wprowadź deadline")

name_label.grid(row=0, column=0)
deadline_label.grid(row=0, column=1)

entry = Entry(entry_frame)
entry.grid(row=1,column=0, padx=5)

deadline_entry = Entry(entry_frame)
deadline_entry.grid(row=1,column=1, padx=5)

# Create Button Frame
button_Frame = Frame(root, bg="#94d3f7")
button_Frame.pack(pady=3)


# logic functions
def delete_item():
    list_box.delete(ANCHOR)


def add_item():
    current_text = entry.get() + "       " + datetime.now().strftime("%m/%d/%Y") + "      " + deadline_entry.get()
    list_box.insert(END, current_text)
    entry.delete(0, END)
    deadline_entry.delete(0,END)


def mark_item():
    list_box.itemconfig(
        list_box.curselection(),
        fg="green"
    )
    list_box.selection_clear(0, END)

    element = list_box.get(ANCHOR)
    delete_item()
    list_box.insert(END, element)


def unmarked_item():
    list_box.itemconfig(
        list_box.curselection(),
        fg="black"
    )
    list_box.selection_clear(0, END)


# function to move marked element in front of a list
def priority_item():
    element = list_box.get(ANCHOR)
    delete_item()
    list_box.insert(0, element)


def deadline_item():
    element = list_box.get(ANCHOR)
    deadline_data = deadline_entry.get()
    new_element = element + "   " + deadline_data
    index = list_box.index(ANCHOR)
    list_box.delete(index)
    list_box.insert(index, new_element)


# buttons
delete_button = Button(button_Frame, text="usuń", command=delete_item)
add_button = Button(button_Frame, text="dodaj ", command=add_item)
mark_button = Button(button_Frame, text="ukończone", command=mark_item)
unmarked_button = Button(button_Frame, text="odznacz", command=unmarked_item)
priority_button = Button(button_Frame, text="nadaj priorytet", command=priority_item)

# placing buttons on a gird

delete_button.grid(row=1, column=0)
add_button.grid(row=2, column=0,pady=6)
mark_button.grid(row=3, column=0)
unmarked_button.grid(row=4, column=0,pady=6)
priority_button.grid(row=5, column=0)


tk.mainloop()
