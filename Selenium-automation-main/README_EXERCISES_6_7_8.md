# פרויקט סיום - אוטומציה Selenium-Java
## פתרון תרגילים 6, 7, 8

### סקירה כללית
פרויקט זה מיישם בדיקות אוטומטיות לאתר ביטוח לאומי תוך שימוש ב-Selenium WebDriver ו-JUnit 5.

---

## 📋 תוכן התרגילים שהוכנו

### ✅ תרגיל 6: חישוב דמי ביטוח לאומי לבחור ישיבה
**קובץ הטסט:** `Test6_InsuranceCalculator.java`

**תהליך הבדיקה:**
1. כניסה לאתר ביטוח לאומי
2. ניווט: דמי ביטוח → דמי ביטוח לאומי
3. בדיקת כותרת הדף
4. כניסה למחשבון לחישוב דמי ביטוח
5. מילוי פרטים לתלמיד ישיבה (גיל 18-70)
6. בדיקת תוצאות:
   - דמי ביטוח לאומי: 43 ש"ח
   - דמי ביטוח בריאות: 120 ש"ח
   - סה"כ: 163 ש"ח

**מחלקות Page Object:**
- `InsurancePage.java` - דף דמי ביטוח לאומי
- `InsuranceCalculatorPage.java` - דף המחשבון

---

### ✅ תרגיל 7: חישוב דמי אבטלה
**קובץ הטסט:** `Test7_UnemploymentCalculator.java`

**תהליך הבדיקה:**
1. כניסה לאתר ביטוח לאומי
2. ניווט: קצבאות והטבות → אבטלה
3. לחיצה על "מחשבוני דמי אבטלה"
4. כניסה ל"חישוב סכום דמי אבטלה"
5. מילוי תאריך הפסקת עבודה (עד חודש אחורה)
6. בחירת גיל מעל 28
7. מילוי סכומי השתכרות
8. בדיקה שמופיעים:
   - שכר יומי ממוצע לצורך חישוב דמי אבטלה
   - דמי אבטלה ליום
   - דמי אבטלה לחודש

**מחלקות Page Object:**
- `UnemploymentPage.java` - דף אבטלה
- `UnemploymentCalculatorPage.java` - דף מחשבון אבטלה

---

### ✅ תרגיל 8: Parametrized Test
**קובץ הטסט:** `BtlParametrizedTest.java`

**תהליך הבדיקה:**
- טסט פרמטרי שבודק ניווט ל-5 דפים שונים תחת "קצבאות והטבות"
- בדיקת תקינות breadcrumbs בכל דף
- הדפים הנבדקים:
  1. אבטלה
  2. ילדים
  3. אזרח ותיק
  4. נכות כללית
  5. אימהות

---

## 🚀 הרצת הטסטים

### דרישות מקדימות
- JDK 8 ואילך
- Maven
- Chrome browser מותקן

### הרצה דרך IntelliJ IDEA
1. פתח את הפרויקט ב-IntelliJ
2. וודא ש-Maven סינכרן את התלויות
3. לחץ ימנית על קובץ הטסט הרצוי
4. בחר `Run 'Test6_InsuranceCalculator'` (או Test7/BtlParametrizedTest)

### הרצה דרך Terminal
```bash
# הרצת כל הטסטים
mvn test

# הרצת תרגיל 6 בלבד
mvn test -Dtest=Test6_InsuranceCalculator

# הרצת תרגיל 7 בלבד
mvn test -Dtest=Test7_UnemploymentCalculator

# הרצת תרגיל 8 בלבד
mvn test -Dtest=BtlParametrizedTest
```

---

## 📁 מבנה הפרויקט

```
src/
├── main/java/org/example/
│   ├── BasePage.java                    # מחלקה אבסטרקטית בסיסית
│   ├── BtlBasePage.java                 # מחלקת בסיס לאתר ביטוח לאומי
│   ├── HomePage.java                    # דף הבית
│   ├── MainMenu.java                    # Enum של התפריט הראשי
│   ├── BranchesPage.java                # דף סניפים
│   ├── BranchDetailsPage.java           # דף פרטי סניף
│   ├── InsurancePage.java               # דף דמי ביטוח לאומי (תרגיל 6)
│   ├── InsuranceCalculatorPage.java     # מחשבון דמי ביטוח (תרגיל 6)
│   ├── UnemploymentPage.java            # דף אבטלה (תרגיל 7)
│   └── UnemploymentCalculatorPage.java  # מחשבון אבטלה (תרגיל 7)
│
└── test/java/org/example/
    ├── Test6_InsuranceCalculator.java   # תרגיל 6: חישוב דמי ביטוח
    ├── Test7_UnemploymentCalculator.java # תרגיל 7: חישוב דמי אבטלה
    ├── BtlParametrizedTest.java         # תרגיל 8: בדיקה פרמטרית
    ├── BtlTest.java                     # טסטים נוספים (סניפים וחיפוש)
    └── BTL.java                         # טסט ישן לדוגמה
```

---

## 🎯 עקרונות עיצוב

### Page Object Model (POM)
הפרויקט עוקב אחר דפוס ה-Page Object Model:
- כל דף באתר מיוצג על ידי מחלקה נפרדת
- האלמנטים מוגדרים עם `@FindBy` annotations
- הפונקציונליות מוסתרת בתוך מתודות של המחלקה

### המתנה דינמית
- שימוש ב-`WebDriverWait` עם timeout של 10 שניות
- המתנה לאלמנטים עם `ExpectedConditions`

### דיווח וצילומי מסך
- צילום מסך אוטומטי במקרה של כישלון טסט
- שמירה בתיקיית `screenshots/`
- שימוש ב-JUnit 5 TestWatcher

---

## 📦 תלויות עיקריות

```xml
<dependencies>
    <!-- Selenium -->
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.16.1</version>
    </dependency>
    
    <!-- WebDriverManager -->
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.6.3</version>
    </dependency>
    
    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.1</version>
    </dependency>
    
    <!-- Commons IO -->
    <dependency>
        <groupId>commons-io</groupId>
        <artifactId>commons-io</artifactId>
        <version>2.13.0</version>
    </dependency>
</dependencies>
```

---

## 💡 הערות חשובות

1. **המתנה לטעינת דפים**: אתר ביטוח לאומי משתמש ב-iframes רבים ובטעינה דינמית. הטסטים כוללים המתנות מתאימות.

2. **אלמנטים דינמיים**: האתר משתמש ב-IDs דינמיים. לכן נעשה שימוש ב-CSS selectors גמישים וב-XPath.

3. **JavaScript Executor**: במקומות מסוימים נדרש שימוש ב-JavaScript לביצוע פעולות (גלילה, לחיצה) כשהפעולות הרגילות של Selenium לא עובדות.

4. **תאריכים**: התרגילים משתמשים בתאריכים דינמיים יחסית לתאריך הנוכחי.

---

## 🐛 פתרון בעיות

### הטסט נכשל על איתור אלמנט
- בדוק שהדפדפן מעודכן לגרסה האחרונה
- ייתכן שהאתר השתנה - בדוק את ה-selectors
- הוסף Thread.sleep() לפני פעולות קריטיות

### הדפדפן לא פותח
- וודא ש-Chrome מותקן
- WebDriverManager אמור להוריד את הדרייבר אוטומטית
- בדוק את חיבור האינטרנט

### שגיאת Timeout
- הגדל את הזמן ב-WebDriverWait (כרגע 10 שניות)
- בדוק את מהירות האינטרנט

---

## ✨ סיכום

כל 3 התרגילים (6, 7, 8) מוכנים ומתועדים היטב!
כל טסט כולל:
- ✅ ניווט מלא מדף הבית
- ✅ בדיקות כותרות ותוכן
- ✅ המתנות דינמיות
- ✅ דיווח מפורט לקונסול
- ✅ צילומי מסך במקרה של כישלון
- ✅ שימוש נכון ב-Page Object Model

**בהצלחה! 🎉**
