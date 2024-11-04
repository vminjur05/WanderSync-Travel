Bloaters (Vignesh) : the calculateTotalPrice method was too long and had too many factors in that method. To solve this bloating, we created a new OrderCalculator class that handles calculations, while the order class was responsible for getting orders out. 

OOA (Jude) : We removed the switch statement and replaced it with polymorphism to control the implementation. This made it so that the OOA condition was not violated. 

Change Preventers (Abhiram) : A separate class was made for discount calculations. This reduced the dependencies amongst all the classes and made it easier to make modifications in the future. 

Dispensable (Jyotir) : The printOrder method prints to the console but is not related to the main responsibilities of an Order class. To fix this we kept the printOrder method in a new class to separate class functionalities. 

Couplers 2 (Andrew) : The calculateTotalPrice method depends on many details of the Item and TaxableItem classes. This close interaction suggests that the Order class knows too much about the internal workings of Item, leading to tight coupling. To combat this we put all the calculations into a separate class, and made Order do all the less specific tasks.
