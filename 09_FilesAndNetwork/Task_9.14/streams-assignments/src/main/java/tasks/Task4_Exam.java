package tasks;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Есть грузовик Truck, у которого задана максимальная грузоподьемность.
 * <p>
 * Грузовики делятся на 3 типа в зависимости от грузоподьемности:
 * - Pickup        - до 2 тонн
 * - SmallBoxTruck - до 12 тонн
 * - SemiTrailer   - до 20 тонн
 */
public class Task4_Exam {

    /**
     * Возвращает тип грузовика с наименьшей грузоподьемностью,
     * который сможет перевести заданный вес.
     * <p>
     * Если вес слишком большой, то метод должен кинуть WeightTooHighException с сообщением "слишком большой вес"
     *
     * <pre>
     * Пример:
     *   1_000 -> Pickup   (для одной тонны достаточно пикапа)
     * </pre>
     * <p>
     * Вы можете решить это задание как через Stream, так и через цикл.
     * Какой код выйдет проще и легче для понимания, тот и используйте.
     * <p>
     *
     * <i>Подсказка: Доступные значения в enum можно перебирать через метод values()</i>
     * <p>Совет: Нажмите Ctrl+Q (Cmd+Q) чтобы увидеть комментарии с форматированием.
     *
     * @param weight
     * @return
     */
    public static TruckType getTypeByWeight(int weight) {
        //если значения загрузки в TruckType идут не в порядке возрастания, то алгоритм работает неправильно. Предварительно необходимо отсортировать.
//        return Arrays.stream(TruckType.values()).filter(truckType -> truckType.canHandleWeight(weight)).findFirst().orElseThrow(WeightTooHighException::new);
        //Такое решение становится более стабильным
        return Arrays.stream(TruckType.values())
                .filter(truckType -> truckType.canHandleWeight(weight))
                .min(Comparator.comparing(TruckType::getMaxLoad)).orElseThrow(WeightTooHighException::new);

    }

    /**
     * Сгруппировать все грузовики по их грузоподьемности.
     *
     * <p>Пример:
     * <pre>
     *      List(Truck(5_000), Truck(5_100), Truck(20_000))
     *      ->
     *      Map [
     *          SmallBoxTruck => List(Truck(5_000), Truck(5_100))
     *          SemiTrailer   => List(Truck(20_000))
     *      ]
     *
     * Понадобиться:
     *   - Stream::collect
     *   - Collectors.groupingBy
     *   - Task3::getTypeByWeight
     * </pre>
     *
     * @param trucks
     * @return
     */
    //Можно было выполнить задание 2 путями:
    //1. Не выбрасывать исключение getTypeByWeight
    //.orElseThrow(WeightTooHighException::new), а вернуть .orElse(TruckType.SemiTrailer)
    //2. Обработать исключение на стороне вызывающего метода groupTrucksByType что я и сделал.
    public static Map<TruckType, List<Truck>> groupTrucksByType(List<Truck> trucks) {
        return trucks.stream().collect(Collectors.groupingBy(truck -> {
            TruckType truckType;
            try {
                truckType = getTypeByWeight(truck.maxWeightKg);
            } catch (WeightTooHighException weightTooHighException) {
                truckType = Arrays.stream(TruckType.values()).max(Comparator.comparing(e -> e.maxLoad)).get();
            }
            return truckType;
        }));
    }

    /**
     * Посчитать кол-во грузовиков в каждой группе.
     *
     * <p>Пример:
     * <pre>
     *      List(Truck(5_000), Truck(5_100), Truck(20_000))
     *      ->
     *      Map [
     *          SmallBoxTruck => 2
     *          SemiTrailer   => 1
     *      ]
     *
     * Понадобиться:
     *   - Stream::collect
     *   - Collectors.groupingBy
     *   - Collectors.counting
     * </pre>
     *
     * @param trucks
     * @return
     */
    public static Map<TruckType, Long> countTrucksByType(List<Truck> trucks) {
        return trucks.stream().collect(Collectors.groupingBy(truck -> getTypeByWeight(truck.maxWeightKg), Collectors.counting()));
    }

    /**
     * Грузовик и его грузоподьемность.
     */
    static class Truck {
        int maxWeightKg;

        Truck(int maxWeightKg) {
            this.maxWeightKg = maxWeightKg;
        }
    }

    /**
     * Тип грузовика по грузоподьемности в кг.
     * <p>
     * Гарантируется, что значения отсортированы по возрастанию. Т.е. можно смело итерироваться по .values()
     */
    enum TruckType {
        Pickup(2_000),
        SmallBoxTruck(12_000),
        SemiTrailer(20_000);

        private int maxLoad;

        TruckType(int maxLoad) {
            this.maxLoad = maxLoad;
        }

        public boolean canHandleWeight(int weight) {
            return weight <= this.maxLoad;
        }

        public int getMaxLoad() { //добавил геттер чтоб можно было сортировать
            return maxLoad;
        }
    }

    public static class WeightTooHighException extends RuntimeException {
        public WeightTooHighException() {
            super("Weight is too high for any type of vehicle available");
        }
    }
}
