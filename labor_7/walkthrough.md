# Walkthrough: Android Fragment & Navigation Implementation

This walkthrough covers the implementation of Labs 5, 6, and 7, focusing on Fragments, Navigation, Backend Integration, and Habit/Schedule Management.

## Implemented Features

### Lab 5: Fragments & Navigation

- **Fragments**: Created `LoginFragment`, `RegisterFragment`, `HomeFragment`, and `ProfileFragment`.
- **Navigation**: Implemented `Navigation Component` with a navigation graph (`nav_graph.xml`) connecting all fragments.
- **Bottom Navigation**: Added a `BottomNavigationView` for easy access to Home and Profile screens.

### Lab 6: Backend Integration (Authentication)

- **Networking**: Set up `Retrofit` with `OkHttp` logging and `AuthInterceptor` for JWT handling.
- **Authentication**: Implemented Login and Register flows using `AuthRepository` and `AuthViewModel`.
- **Session Management**: Created `SessionManager` to securely store and retrieve JWT tokens.

### Lab 7: Habit & Schedule Management

- **Data Models**: Defined models for `Habit`, `Schedule`, `HabitCategory`, etc.
- **API Integration**: Updated `ApiService` to support fetching schedules, habits, and creating new ones.
- **Repositories**: Created `HabitRepository` and `ScheduleRepository` to handle data operations.
- **Home Screen**: Implemented a `RecyclerView` in `HomeFragment` to display daily schedules.
- **Add Habit**: Created `AddHabitFragment` to allow users to create new habits with categories.
- **Create Schedule**: Created `CreateScheduleFragment` to allow users to schedule habits for specific times.

## Verification Steps

### 1. Authentication

1. Launch the app.
2. Navigate to the **Login** screen.
3. Enter valid credentials (or register a new account).
4. Verify successful login redirects to the **Home** screen.

### 2. Home Screen & Navigation

1. Verify the **Bottom Navigation** bar allows switching between Home and Profile.
2. Verify the **Home** screen displays a list of schedules for the current day (or an empty state message).

### 3. Creating a Habit

1. On the Home screen, click the **Add Habit** FAB (top one).
2. Enter Name, Description, Goal, and select a Category.
3. Click **Save Habit**.
4. Verify a success message is shown and you are navigated back.

### 4. Scheduling a Habit

1. On the Home screen, click the **Add Schedule** FAB (bottom one).
2. Select a Habit from the dropdown.
3. Enter Start Time (ISO format, e.g., `2025-10-26T10:00:00`), Duration, and Notes.
4. Click **Create Schedule**.
5. Verify a success message is shown and you are navigated back.
6. Verify the new schedule appears on the Home screen (you may need to refresh or restart the app if live update isn't implemented).

## Code Highlights

### ApiService

```kotlin
interface ApiService {
    @POST("/auth/local/signin")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @GET("/schedule/day")
    suspend fun getScheduleByDay(@Query("date") day: String): List<ScheduleResponse>

    @POST("/habit")
    suspend fun createHabit(@Body habit: Map<String, Any>): Response<Habit>
}
```

### HomeFragment (RecyclerView Setup)

```kotlin
private fun setupUi() {
    adapter = HomeScheduleAdapter()
    binding.rvSchedules.layoutManager = LinearLayoutManager(requireContext())
    binding.rvSchedules.adapter = adapter
    // ...
    viewModel.getScheduleByDay(today)
}
```
