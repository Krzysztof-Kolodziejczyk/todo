from tkinter import *
from datetime import datetime

tk = Tk()

tk.title("todo Project")
tk.geometry("300x400")
tk.configure(background="#94d3f7")

font = ("Courier", 15, "italic")

# creating app frame

box_frame = Frame()
box_frame.pack()

# creating listbox

list_box = Listbox(
    box_frame,
    font=font,
    width=28,
    height=9,
    bg="SystemButtonFace",
    bd=3,
    fg="black",
    highlightthickness=0,
    background="white"
)

list_box.pack(side=RIGHT, fill=BOTH)

tasks_list = []

# adding tasks to list_box
for task in tasks_list:
    list_box.insert(END, task)

# Create scrollbar
scrollBar = Scrollbar(box_frame)
scrollBar.pack(side=LEFT, fill=BOTH)

# Add scrollBar
list_box.config(yscrollcommand=scrollBar)
scrollBar.config(command=list_box.yview())

# create entry box to add item to the list
entry = Entry(
    tk,
    font=("Helveticka", 24)
)

entry.pack(pady=10)

# Create Button Frame
button_Frame = Frame(tk)
button_Frame.pack(pady=3)


# logic functions
def delete_item():
    list_box.delete(ANCHOR)
    index = list_box.index(ANCHOR)
    if index == 0:
        for i in range(index, list_box.index(END)):
            tmp = list_box.get(i)[3:]
            tmp = str(i + 1) + ". " + tmp
            list_box.delete(i)
            list_box.insert(i, tmp)
    else:
        for i in range(index - 1, list_box.index(END)):
            print(list_box.get(i))
            tmp = list_box.get(i)[3:]
            tmp = str(i + 1) + ". " + tmp
            list_box.delete(i)
            list_box.insert(i, tmp)


def add_item():
    current_index = list_box.index(END)
    current_index += 1
    current_text = str(current_index) + ". " + entry.get() + "       " + datetime.now().strftime("%m/%d/%Y")
    list_box.insert(END, current_text)
    entry.delete(0, END)


def mark_item():
    list_box.itemconfig(
        list_box.curselection(),
        fg="green"
    )
    list_box.selection_clear(0,END)


def unmarked_item():
    list_box.itemconfig(
        list_box.curselection(),
        fg="black"
    )
    list_box.selection_clear(0,END)


# function to move marked element in front of a list
def priority_item():
    element = list_box.get(ANCHOR)
    delete_item()
    tmp = element[3:]
    tmp = "1. " + tmp
    for i in range(list_box.size()):
        element = list_box.get(i)[3:]
        new_element = str(i + 2) + ". " + element
        list_box.delete(i)
        list_box.insert(i, new_element)
    list_box.insert(0, tmp)


# buttons
delete_button = Button(button_Frame, text="usuń", command=delete_item)
add_button = Button(button_Frame, text="dodaj ", command=add_item)
mark_button = Button(button_Frame, text="ukończone", command=mark_item)
unmarked_button = Button(button_Frame, text="odznacz", command=unmarked_item)
priority_button = Button(button_Frame, text="nadaj priorytet", command=priority_item)


# placing buttons on a gird
add_button.grid(row=0, column=0, pady=3)
delete_button.grid(row=1, column=0)
mark_button.grid(row=2, column=0, pady=3)
unmarked_button.grid(row=3, column=0)
priority_button.grid(row=4, column=0, pady=3)

tk.mainloop()
