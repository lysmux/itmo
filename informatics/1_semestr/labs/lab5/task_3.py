import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns

df = pd.read_csv("task_2.csv", delimiter=";")

df_melted = df.melt(
    id_vars="Дата",
    value_vars=["Открытие", "Макс", "Мин", "Закрытие"],
    var_name="Type",
    value_name="Value"
)

plt.figure(figsize=(10, 6))
sns.boxplot(
    data=df_melted,
    x="Дата",
    y="Value",
    hue="Type",
    palette=["blue", "orange", "gray", "yellow"]
)

plt.title("Статистика")
plt.ylabel("Значение")
plt.xlabel("Дата")
plt.legend(loc="upper right")

plt.savefig("task_3.png")
plt.show()

