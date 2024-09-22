<h3><b>Title</b> : Static imports</h3>
<b>Date</b> : 06.04.2021<br>
<b>Author</b> : Maciej Lumanisha<br>
<b>Description</b> :<br>

For readability purposes I suggest using static imports for enums and static functions.

Example

```java
import static pl.itcraft.servicebox.core.entities.BillingEventFaker.BILLING_EVENT_LIST_FIRST_HANDOVER;
...
...
...
DailyEquipmentCost result = sut.calculateFirstBillingPeriod(BILLING_EVENT_LIST_FIRST_HANDOVER, billingDate)
```

instead of

```java
DailyEquipmentCost result = sut.calculateFirstBillingPeriod(BillingEventFaker.BILLING_EVENT_LIST_FIRST_HANDOVER, billingDate)
```


