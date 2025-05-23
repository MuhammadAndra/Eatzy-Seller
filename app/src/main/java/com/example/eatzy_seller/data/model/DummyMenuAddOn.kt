package com.example.eatzy_seller.data.model


fun fetchCategoryById(id: Int): AddOnCategory {
    return AddOnCategory(
        addOnCategoryId = 1,  // Assuming you can convert String to Int
        addOnCategoryName = "Sambal",
        addOnCategoryMultiple = false,  // Since isSingleChoice was true
        addOns = dummyAddOns1
    )
}

// Dummy AddOn data
val dummyAddOns1 = listOf(
    AddOn(1, "Extra Cheese", 5000.0, true),
    AddOn(2, "Extra Meat", 7000.0, true),
    AddOn(3, "Mushrooms", 4000.0, false)
)

val dummyAddOns2 = listOf(
    AddOn(4, "Spicy Level 1", 0.0, true),
    AddOn(5, "Spicy Level 2", 0.0, true),
    AddOn(6, "Extra Spicy", 3000.0, true)
)

val dummyAddOns3 = listOf(
    AddOn(7, "Ice Cubes", 0.0, true),
    AddOn(8, "Extra Sugar", 0.0, true),
    AddOn(9, "Pearls", 4000.0, true)
)

// Dummy AddOnCategory data
val dummyAddOnCategories = listOf(
    AddOnCategory(1, "Toppings", true, dummyAddOns1),
    AddOnCategory(2, "Spiciness", false, dummyAddOns2),
    AddOnCategory(3, "Drink Options", true, dummyAddOns3)
)

// Dummy Menu data
val dummyMenus1 = listOf(
    Menu(1, "Cheese Burger", 25000.0,25, "burger_img", true, dummyAddOnCategories),
    Menu(2, "Chicken Burger", 22000.0,25, "chicken_burger_img", true, dummyAddOnCategories),
    Menu(3, "Veggie Burger", 20000.0,25, "veggie_burger_img", false, dummyAddOnCategories)
)

val dummyMenus2 = listOf(
    Menu(4, "Spaghetti Carbonara", 30000.0,25, "burger_img", true, dummyAddOnCategories),
    Menu(5, "Spaghetti Bolognese", 32000.0,25, "burger_img", true, dummyAddOnCategories),
    Menu(6, "Spaghetti Aglio Olio", 28000.0,25, "burger_img", false, dummyAddOnCategories),
)

val dummyMenus3 = listOf(
    Menu(7, "Iced Tea", 10000.0, 25, "burger_img", true, dummyAddOnCategories),
    Menu(8, "Orange Juice", 12000.0,25, "burger_img", true, dummyAddOnCategories),
    Menu(9, "Coffee Latte", 15000.0,25, "burger_img", true, dummyAddOnCategories),
)

val dummyMenus4 = listOf(
    Menu(10, "Chocolate Cake", 18000.0,25, "burger_img", true, dummyAddOnCategories),
    Menu(11, "Cheesecake", 20000.0,25, "burger_img", true, dummyAddOnCategories),
    Menu(12, "Tiramisu", 22000.0,25, "burger_img", true, dummyAddOnCategories),
)

// Dummy MenuCategory data
val dummyMenuCategories = listOf(
    MenuCategory(1, 1, "Burgers", dummyMenus1),
    MenuCategory(2, 1, "Pasta", dummyMenus2),
    MenuCategory(3, 1, "Drinks", dummyMenus3),
    MenuCategory(4, 1, "Desserts", dummyMenus4)
)