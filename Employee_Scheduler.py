import random

DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
SHIFTS = ["Morning", "Afternoon", "Evening"]

# Get number of employees
while True:
    try:
        num_employees = int(input("Enter number of employees: "))
        if num_employees < 1:
            print("Number must be greater than 0.")
            continue
        if num_employees < 9:
            choice = input("Not enough employees to cover all shifts for 7 days. Do you want to continue? (Yes/No): ").strip().lower()
            if choice == "no":
                continue
        break
    except ValueError:
        print("Please enter a valid number.")

# Collect employee info
employees = {}
for i in range(num_employees):
    while True:
        name = input(f"Enter employee {i+1} name: ").strip()
        if name:
            break
        print("Employee name cannot be blank.")
    preferences = {}
    print(f"Enter {name}'s preferred shifts for each day (Morning/Afternoon/Evening):")
    for day in DAYS:
        pref = input(f"  {day}: ").strip().capitalize()
        if pref not in SHIFTS:
            print("Invalid input. Assigning random shift.")
            pref = random.choice(SHIFTS)
        preferences[day] = pref
    employees[name] = {"preferences": preferences, "assigned_days": 0}

# Initialize schedule
schedule = {day: {shift: [] for shift in SHIFTS} for day in DAYS}

# Assign shifts
for day in DAYS:
    for emp, data in employees.items():
        if data["assigned_days"] >= 5:
            continue
        pref = data["preferences"][day]
        if len(schedule[day][pref]) < 2 and emp not in schedule[day][pref]:
            schedule[day][pref].append(emp)
            data["assigned_days"] += 1
        else:
            # Try another shift same day
            assigned = False
            for alt in SHIFTS:
                if len(schedule[day][alt]) < 2 and emp not in schedule[day][alt]:
                    schedule[day][alt].append(emp)
                    data["assigned_days"] += 1
                    assigned = True
                    break
            # Try next day if not possible today
            if not assigned:
                next_day = DAYS[(DAYS.index(day) + 1) % len(DAYS)]
                for alt in SHIFTS:
                    if len(schedule[next_day][alt]) < 2 and emp not in schedule[next_day][alt]:
                        schedule[next_day][alt].append(emp)
                        data["assigned_days"] += 1
                        break

# Fill missing slots ensuring unique employees
for day in DAYS:
    for shift in SHIFTS:
        while len(schedule[day][shift]) < 2:
            available = [e for e, d in employees.items() if d["assigned_days"] < 5 and e not in schedule[day][shift]]
            if not available:
                break
            chosen = random.choice(available)
            schedule[day][shift].append(chosen)
            employees[chosen]["assigned_days"] += 1

# Print final schedule: Days as rows, Shifts as columns
print("\nFinal Weekly Schedule:\n")
header = f"{'Day':<12}{'Morning':<25}{'Afternoon':<25}{'Evening':<25}"
print(header)
print("-" * len(header))

for day in DAYS:
    row = f"{day:<12}"
    for shift in SHIFTS:
        val = ", ".join(schedule[day][shift])
        row += f"{val:<25}"
    print(row)
