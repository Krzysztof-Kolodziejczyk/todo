from tkinter import *
import tkinter as tk


def klik():
    print(stan.get())


def rbklik():
    print(stan.get())


def rysuj(event):
    if kolor.get() == 1:
        kol = 'black'
    if kolor.get() == 2:
        kol = 'pink'
    if stan2.get() == 3:
        szerokosc = 1
        wysokosc = 1
    if stan2.get() == 4:
        szerokosc = 5
        wysokosc = 5
    if stan.get() == 5:
        canvas.create_rectangle((event.x - szerokosc, event.y - wysokosc, event.x + szerokosc, event.y + wysokosc),
                                fill=kol, outline=kol)
    if stan.get() == 6:
        canvas.create_polygon((event.x - szerokosc, event.y - wysokosc, event.x + szerokosc, event.y + wysokosc),
                              fill=kol, outline=kol)


root = Tk()
okno = tk.Tk()
okno.title('Rysunek')
okno.geometry('525x525')
kolor = tk.IntVar()
stan = tk.IntVar()
stan2 = tk.IntVar()
rb1 = tk.Radiobutton(okno, text='Kwadrat', variable=stan, value=5, command=rbklik)
rb1.place(x=100, y=50)
rb2 = tk.Radiobutton(okno, text='Trapez', variable=stan, value=6, command=rbklik)
rb2.place(x=100, y=100)
rb3 = tk.Radiobutton(okno, text='Cienka linia', variable=stan2, value=3, command=rbklik)
rb3.place(x=340, y=50)
rb4 = tk.Radiobutton(okno, text='Gruba linia', variable=stan2, value=4, command=rbklik)
rb4.place(x=340, y=100)
rb5 = tk.Radiobutton(okno, text='Czarny', variable=kolor, value=1)
rb5.place(x=230, y=50)
rb6 = tk.Radiobutton(okno, text='Różowy', variable=kolor, value=2)
rb6.place(x=230, y=100)
canvas = tk.Canvas(okno, width=600, height=600)
canvas.place(x=100, y=150)
canvas.create_text((50, 10), text='tekst', font=('Times new roman', 22))
canvas.create_rectangle((0, 0, 350, 300), fill='white')
canvas.bind('<B1-Motion>', rysuj)
scrollBar = Scrollbar(okno)
scrollBar.pack(side=RIGHT, fill=BOTH)
canvas.config(yscrollcommand=scrollBar)
scrollBar.config(command=root.yview())

okno.mainloop()
