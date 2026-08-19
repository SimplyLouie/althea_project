# Code Explanation — Monthly Electric Bill Estimator

Companion notes for `ElectricBillEstimator.java`. Every line number below refers
to that file.

---

## Purpose

The program estimates a household's monthly electric bill from two meter
readings, classifies the consumption into a rate bracket, and warns the user
when the consumption looks unusually high for the household size and the season.

---

## Required concepts and where they appear

| Concept | Line(s) |
|---|---|
| `Scanner` | 7 |
| Variables and data types | 9-20 |
| User input (6 inputs) | 27-42 |
| `if` | 47 |
| `else if` | 63, 67, 103, 114 |
| `else` | 55, 71, 108, 119 |
| Nested `if` | 98 (inside the `if` on 96, itself inside the `else` on 55) |
| Logical operators | `\|\|` on 47 and 103; `&&` on 96, 98, 114 |

The project requires at least 5 user inputs. This program uses 6.

---

## What each variable represents

| Variable | Type | Meaning |
|---|---|---|
| `customerName` | `String` | Name of the person being billed |
| `previousReading` | `double` | Last month's meter reading, in kWh |
| `currentReading` | `double` | This month's meter reading, in kWh |
| `householdSize` | `int` | How many people live in the house |
| `billingMonth` | `int` | Month number, 1 = January to 12 = December |
| `ratePerKwh` | `double` | Base charge for every kilowatt-hour, in pesos |
| `kwhUsed` | `double` | Total electricity consumed this month |
| `effectiveRate` | `double` | Base rate after the discount or surcharge is applied |
| `totalBill` | `double` | Final amount the household has to pay |
| `kwhPerPerson` | `double` | Consumption divided by household size |
| `usageLevel` | `String` | Text label for the consumption bracket |

`double` is used for readings, rates and money because these can have decimal
places. `int` is used for the household size and month number because those are
always whole numbers.

---

## Line-by-line reasoning

### Line 7 — the Scanner
`Scanner input = new Scanner(System.in);` connects the program to the keyboard so
the user can type values in. It is closed at the end with `input.close()`.

### Line 28 — reading the name
`nextLine()` is used for the name because a name can contain spaces.
`nextDouble()` and `nextInt()` are used for the numbers that follow.

### Line 47 — validation `if` (uses `||`)
```java
if (currentReading < previousReading || householdSize <= 0 || ratePerKwh <= 0)
```
An electric meter only counts upward, so the current reading can never be lower
than the previous one. The household size must be at least 1 person, and the
rate must be greater than zero, or the arithmetic would be meaningless.

`||` (OR) is used so that **any single** bad value is enough to stop the program.
If all three checks pass, control moves to the `else` on line 55 where the real
work happens.

### Line 57 — the computation
Consumption is the difference between the two meter readings.

### Lines 59-73 — rate brackets (`if` / `else if` / `else`)
| Consumption | Label | Rate applied |
|---|---|---|
| 100 kWh or less | LIFELINE | base rate minus 20% |
| 101 to 300 kWh | NORMAL | plain base rate |
| 301 to 500 kWh | HIGH | base rate plus 15% |
| more than 500 kWh | VERY HIGH | base rate plus 30% |

`else if` is the correct structure here because the brackets are mutually
exclusive: exactly one of them can apply to any given consumption. Low users are
rewarded with a discount and heavy users pay a surcharge, which is how tiered
electricity pricing normally works.

Because each branch is checked in order, `else if (kwhUsed <= 300)` only runs
when the first test already failed, so it effectively means "between 101 and 300".

### Line 96 — the outer assessment check (uses `&&`)
```java
if (kwhUsed > 300 && kwhPerPerson > 120)
```
A household is only investigated for a spike when it is **both** a heavy user
overall **and** heavy per individual person. `&&` (AND) requires both to be true
at once. This matters because a large family naturally uses more electricity
than a small one, so total consumption alone would unfairly flag big households.

### Line 98 — the nested `if`
This `if` sits **inside** the `if` on line 96, which itself sits inside the
`else` on line 55. Once the program knows consumption is genuinely high, this
inner block decides *why*:

- **Line 98** — `billingMonth >= 3 && billingMonth <= 5` covers March, April and
  May, the hottest months, when electric fans and air conditioners run longest.
  High usage is expected, so the advice is about cooling settings.
- **Line 103** — `householdSize >= 5 || kwhUsed > 700` catches a large household
  or a very heavy load. `||` is used because either reason on its own explains
  the consumption.
- **Line 108** — the `else` is the genuine anomaly: high usage that the season
  cannot explain and the household size cannot explain. This is the only case
  that recommends inspecting the wiring.

### Line 114 — the efficiency compliment (uses `&&`)
`kwhUsed <= 100 && householdSize >= 4` recognises a household of four or more
that still stayed under 100 kWh, which is genuinely efficient.

### Line 119 — the final `else`
Everything that is neither high nor unusually low falls here and gets a neutral
"within the expected range" message. Having a final `else` guarantees the user
always receives an assessment, no matter what they typed.

---

## Tested behaviour

All six cases below were compiled and run successfully.

| # | Readings | People | Month | Bracket | Rate | Total bill | Assessment branch |
|---|---|---|---|---|---|---|---|
| 1 | 1000 to 1050 (50 kWh) | 4 | 7 | LIFELINE | 9.60 | 480.00 | Efficient household |
| 2 | 1000 to 1200 (200 kWh) | 3 | 7 | NORMAL | 12.00 | 2,400.00 | Within expected range |
| 3 | 1000 to 1450 (450 kWh) | 2 | 4 | HIGH | 13.80 | 6,210.00 | Hot season |
| 4 | 1000 to 1450 (450 kWh) | 2 | 7 | HIGH | 13.80 | 6,210.00 | Wiring warning |
| 5 | 1000 to 1800 (800 kWh) | 6 | 9 | VERY HIGH | 15.60 | 12,480.00 | Large household |
| 6 | 1500 to 1200 (negative) | 3 | 7 | — | — | — | Rejected as invalid |

A base rate of 12.00 pesos per kWh was used for every test.

**Cases 3 and 4 are the ones to demonstrate.** Every input is identical except
the month, and the nested `if` on line 98 sends them down different paths. That
single comparison shows both where the nested `if` is and what happens when the
user enters a different value.

---

## How to run

```bash
javac ElectricBillEstimator.java
java ElectricBillEstimator
```

Then type the six answers at the prompts, pressing Enter after each one.

In NetBeans, put the file in the project's `src` folder and press Shift+F6.
