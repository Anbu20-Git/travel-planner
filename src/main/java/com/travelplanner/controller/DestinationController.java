package com.travelplanner.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.travelplanner.service.DestinationService;

@Controller
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping("/destinations")
    public String destinations(Model model) {
        model.addAttribute(
                "destinations",
                destinationService.getAllDestinations()
        );

        return "destinations";
    }

    @GetMapping("/destinations/{destinationName}/explore")
    public String exploreDestination(
            @PathVariable String destinationName,
            Model model) {

        String key = destinationName.trim().toLowerCase();

        Map<String, List<List<Map<String, String>>>> data =
                getDestinationData();

        if (!data.containsKey(key)) {
            return "redirect:/destinations";
        }

        Map<String, String> countries = Map.of(
                "paris", "France",
                "dubai", "United Arab Emirates",
                "bali", "Indonesia",
                "tokyo", "Japan",
                "london", "United Kingdom",
                "singapore", "Singapore"
        );

        String displayName = key.substring(0, 1).toUpperCase()
                + key.substring(1);

        model.addAttribute("destinationName", displayName);
        model.addAttribute("country", countries.get(key));
        model.addAttribute("places", data.get(key).get(0));
        model.addAttribute("foods", data.get(key).get(1));

        return "destination-explore";
    }

    private Map<String, List<List<Map<String, String>>>> getDestinationData() {

        return Map.of(

            "paris", List.of(
                List.of(
                    item("Eiffel Tower", "Iconic landmark with spectacular city views."),
                    item("Louvre Museum", "World-famous art museum."),
                    item("Notre-Dame Cathedral", "Historic Gothic cathedral."),
                    item("Arc de Triomphe", "Famous monument in Paris."),
                    item("Montmartre", "Artistic neighbourhood with charming streets."),
                    item("Sacré-Cœur Basilica", "Basilica overlooking Paris."),
                    item("Musée d'Orsay", "Museum famous for Impressionist art."),
                    item("Luxembourg Gardens", "Beautiful gardens and fountains."),
                    item("Seine River", "Enjoy riverside walks and boat cruises."),
                    item("Palace of Versailles", "Historic royal palace near Paris.")
                ),
                List.of(
                    item("Croissant", "Buttery, flaky French pastry."),
                    item("Baguette", "Traditional French bread."),
                    item("Crêpes", "Thin pancakes with sweet or savoury fillings."),
                    item("French Onion Soup", "Traditional onion soup with bread and cheese."),
                    item("Macarons", "Colourful almond-based pastries."),
                    item("Escargots", "Traditional French snail dish."),
                    item("Steak Frites", "Steak served with fries."),
                    item("French Cheese", "Famous regional French cheeses."),
                    item("Crème Brûlée", "Custard topped with caramelised sugar."),
                    item("Pain au Chocolat", "Chocolate-filled pastry.")
                )
            ),

            "dubai", List.of(
                List.of(
                    item("Burj Khalifa", "Famous skyscraper with observation decks."),
                    item("Dubai Mall", "Shopping, dining and entertainment."),
                    item("Palm Jumeirah", "Famous palm-shaped island."),
                    item("Dubai Marina", "Waterfront promenade and skyline."),
                    item("Museum of the Future", "Explore futuristic technology."),
                    item("Dubai Frame", "Views of old and modern Dubai."),
                    item("Al Fahidi District", "Traditional Emirati architecture."),
                    item("Dubai Creek", "Historic trading waterway."),
                    item("Jumeirah Mosque", "Beautiful Islamic architecture."),
                    item("Dubai Desert", "Desert scenery and safari experiences.")
                ),
                List.of(
                    item("Shawarma", "Seasoned meat wrapped in flatbread."),
                    item("Al Machboos", "Spiced rice with meat or seafood."),
                    item("Luqaimat", "Sweet dumplings with syrup."),
                    item("Harees", "Traditional wheat and meat dish."),
                    item("Mandi", "Fragrant rice with slow-cooked meat."),
                    item("Knafeh", "Sweet Middle Eastern cheese pastry."),
                    item("Falafel", "Crispy chickpea fritters."),
                    item("Hummus", "Creamy chickpea dip."),
                    item("Karak Chai", "Milk tea with aromatic spices."),
                    item("Arabic Mixed Grill", "Grilled meats with traditional accompaniments.")
                )
            ),

            "bali", List.of(
                List.of(
                    item("Uluwatu Temple", "Clifftop temple overlooking the ocean."),
                    item("Tanah Lot", "Famous sea temple."),
                    item("Ubud Monkey Forest", "Forest sanctuary with ancient temples."),
                    item("Tegallalang Rice Terraces", "Beautiful terraced rice fields."),
                    item("Mount Batur", "Volcanic landscape and sunrise hikes."),
                    item("Tirta Empul Temple", "Sacred water temple."),
                    item("Ubud Art Market", "Traditional crafts and souvenirs."),
                    item("Sekumpul Waterfall", "Waterfall surrounded by greenery."),
                    item("Nusa Penida", "Island with dramatic coastal scenery."),
                    item("Besakih Temple", "Important Balinese temple complex.")
                ),
                List.of(
                    item("Nasi Goreng", "Indonesian fried rice."),
                    item("Mie Goreng", "Indonesian stir-fried noodles."),
                    item("Babi Guling", "Balinese-style roasted pork."),
                    item("Sate Lilit", "Balinese minced-meat satay."),
                    item("Ayam Betutu", "Slow-cooked spiced chicken."),
                    item("Lawar", "Traditional vegetable and meat mixture."),
                    item("Nasi Campur", "Rice with assorted side dishes."),
                    item("Pisang Goreng", "Fried banana snack."),
                    item("Klepon", "Rice cakes filled with palm sugar."),
                    item("Jaje Bali", "Traditional Balinese sweets.")
                )
            ),

            "tokyo", List.of(
                List.of(
                    item("Tokyo Skytree", "Panoramic views from a landmark tower."),
                    item("Senso-ji Temple", "Historic Buddhist temple."),
                    item("Shibuya Crossing", "Famous pedestrian crossing."),
                    item("Meiji Shrine", "Peaceful Shinto shrine surrounded by trees."),
                    item("Imperial Palace", "Historic palace grounds."),
                    item("Shinjuku Gyoen", "Large landscaped garden."),
                    item("Akihabara", "Anime, electronics and gaming culture."),
                    item("Tsukiji Outer Market", "Japanese food stalls and seafood."),
                    item("Ueno Park", "Park with museums and gardens."),
                    item("teamLab Planets", "Immersive digital art experience.")
                ),
                List.of(
                    item("Sushi", "Vinegared rice with fish or vegetables."),
                    item("Ramen", "Noodles served in a flavourful broth."),
                    item("Tempura", "Lightly battered fried seafood or vegetables."),
                    item("Tonkatsu", "Breaded and fried pork cutlet."),
                    item("Yakitori", "Grilled chicken skewers."),
                    item("Soba", "Traditional buckwheat noodles."),
                    item("Okonomiyaki", "Savoury Japanese pancake."),
                    item("Taiyaki", "Fish-shaped pastry with sweet filling."),
                    item("Onigiri", "Rice balls with assorted fillings."),
                    item("Dorayaki", "Pancakes filled with sweet bean paste.")
                )
            ),

            "london", List.of(
                List.of(
                    item("Big Ben", "Famous clock tower in Westminster."),
                    item("Tower Bridge", "Iconic bridge over the Thames."),
                    item("Buckingham Palace", "Official royal residence."),
                    item("British Museum", "Collections from world history."),
                    item("London Eye", "Observation wheel with city views."),
                    item("Tower of London", "Historic fortress and Crown Jewels."),
                    item("Westminster Abbey", "Historic royal church."),
                    item("Trafalgar Square", "Famous public square."),
                    item("Hyde Park", "Large park for walking and relaxing."),
                    item("Natural History Museum", "Museum with natural history exhibits.")
                ),
                List.of(
                    item("Fish and Chips", "Battered fish served with chips."),
                    item("Full English Breakfast", "Traditional cooked breakfast."),
                    item("Sunday Roast", "Roasted meat with vegetables and gravy."),
                    item("Chicken Tikka Masala", "Popular British curry dish."),
                    item("Pie and Mash", "Traditional British comfort food."),
                    item("Bangers and Mash", "Sausages with mashed potatoes."),
                    item("Afternoon Tea", "Tea with sandwiches and pastries."),
                    item("Scotch Egg", "Egg wrapped in sausage meat and breadcrumbs."),
                    item("Scones", "Baked treats served with cream and jam."),
                    item("Sticky Toffee Pudding", "Dessert with rich toffee sauce.")
                )
            ),

            "singapore", List.of(
                List.of(
                    item("Marina Bay Sands", "Famous waterfront resort and skyline."),
                    item("Gardens by the Bay", "Futuristic gardens and conservatories."),
                    item("Sentosa Island", "Beaches and entertainment attractions."),
                    item("Merlion Park", "Home of Singapore's iconic Merlion."),
                    item("Singapore Zoo", "Wildlife in a landscaped zoo."),
                    item("Chinatown", "Heritage streets, shops and temples."),
                    item("Little India", "Colourful streets and Indian culture."),
                    item("Jewel Changi Airport", "Famous indoor waterfall and gardens."),
                    item("Clarke Quay", "Lively riverside district."),
                    item("Singapore Botanic Gardens", "UNESCO-listed tropical garden.")
                ),
                List.of(
                    item("Hainanese Chicken Rice", "Chicken with fragrant rice."),
                    item("Chilli Crab", "Crab in a spicy tomato-based sauce."),
                    item("Laksa", "Noodle soup with a coconut-based broth."),
                    item("Char Kway Teow", "Stir-fried flat rice noodles."),
                    item("Satay", "Grilled meat skewers with peanut sauce."),
                    item("Kaya Toast", "Toast with coconut jam and butter."),
                    item("Roti Prata", "Flaky flatbread served with curry."),
                    item("Bak Kut Teh", "Pork rib soup with herbs and spices."),
                    item("Nasi Lemak", "Coconut rice with accompaniments."),
                    item("Ice Kacang", "Shaved ice dessert with colourful toppings.")
                )
            )
        );
    }

    private static Map<String, String> item(
            String name,
            String description) {

        return Map.of(
                "name", name,
                "description", description
        );
    }
}