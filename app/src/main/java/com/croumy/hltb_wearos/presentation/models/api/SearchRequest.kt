data class SearchRequest(
    val searchType: String = "games",
    val searchTerms: List<String> = listOf(""),
    val searchPage: Int = 1,
    val size: Int = 20,
    val searchOptions: SearchOptions= SearchOptions()
)

data class SearchOptionsGame(
    val userId: Int = 0,
    val platform: String = "",
    val sortCategory: String = "popular",
    val rangeCategory: String = "main",
    val rangeTime: RangeTime = RangeTime(),
    val gameplay: Gameplay = Gameplay(),
    val rangeYear: RangeYear = RangeYear(),
    val modifier: String = ""
)

data class RangeTime(
    val min: String? = null,
    val max: String? = null
)

data class Gameplay(
    val perspective: String = "",
    val flow: String = "",
    val genre: String = ""
)

data class RangeYear(
    val min: String = "",
    val max: String = ""
)

data class SearchOptions(
    val games: SearchOptionsGame = SearchOptionsGame(),
    val users: SortCategory = SortCategory(sortCategory = "postCount"),
    val lists: SortCategory = SortCategory(sortCategory = "follows"),
    val filter: String = "",
    val sort: Int = 0,
    val randomizer: Int = 0
)

data class SortCategory(
    val sortCategory: String,
)