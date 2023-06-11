package efs.task.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ClassInspector {

  /**
   * Metoda powinna wyszukać we wszystkich zadeklarowanych przez klasę polach te które oznaczone
   * są adnotacją podaną jako drugi parametr wywołania tej metody. Wynik powinien zawierać tylko
   * unikalne nazwy pól (bez powtórzeń).
   *
   * @param type       klasa (typ) poddawana analizie
   * @param annotation szukana adnotacja
   * @return lista zawierająca tylko unikalne nazwy pól oznaczonych adnotacją
   */
  public static Collection<String> getAnnotatedFields(final Class<?> type,
      final Class<? extends Annotation> annotation)
  {
    //TODO usuń zawartość tej metody i umieść tutaj swoje rozwiązanie
    return Stream.of(type.getDeclaredFields())
            .filter(x -> x.isAnnotationPresent(annotation))
            .map(Field::getName)
            .distinct()
            .collect(Collectors.toList());
  }

  /**
   * Metoda powinna wyszukać wszystkie zadeklarowane bezpośrednio w klasie metody oraz te
   * implementowane przez nią pochodzące z interfejsów, które implementuje. Wynik powinien zawierać
   * tylko unikalne nazwy metod (bez powtórzeń).
   *
   * @param type klasa (typ) poddawany analizie
   * @return lista zawierająca tylko unikalne nazwy metod zadeklarowanych przez klasę oraz te
   * implementowane
   */
  public static Collection<String> getAllDeclaredMethods(final Class<?> type)
  {
    //TODO usuń zawartość tej metody i umieść tutaj swoje rozwiązanie
    Set<String> result = new HashSet<>();
    result.addAll(Stream.of(type.getDeclaredMethods())
            .map(Method::getName)
            .collect(Collectors.toSet()));

    for(Class<?> x : type.getInterfaces())
    {
       Stream.of(x.getDeclaredMethods())
               .map(Method::getName)
               .forEach(result::add);
    }

    return new ArrayList<>(result);
  }

  /**
   * Metoda powinna odszukać konstruktor zadeklarowany w podanej klasie który przyjmuje wszystkie
   * podane parametry wejściowe. Należy tak przygotować implementację aby nawet w przypadku gdy
   * pasujący konstruktor jest prywatny udało się poprawnie utworzyć nową instancję obiektu
   * <p>
   * Przykładowe użycia:
   * <code>ClassInspector.createInstance(Villager.class)</code>
   * <code>ClassInspector.createInstance(Villager.class, "Nazwa", "Opis")</code>
   *
   * @param type klasa (typ) którego instancje ma zostać utworzona
   * @param args parametry które mają zostać przekazane do konstruktora
   * @return nowa instancja klasy podanej jako parametr zainicjalizowana podanymi parametrami
   * @throws Exception wyjątek spowodowany nie znalezieniem odpowiedniego konstruktora
   */
  public static <T> T createInstance(final Class<T> type, final Object... args) throws Exception
  {
    //TODO usuń zawartość tej metody i umieść tutaj swoje rozwiązanie
    Optional<Constructor<?>> construct = Stream.of(type.getDeclaredConstructors())
                              .filter(x -> x.getParameterCount() == args.length
                                      && IntStream.range(0, args.length)
                                      .allMatch(i -> x.getParameterTypes()[i].isInstance(args[i]))
                              )
                              .findFirst();
    if (construct.isPresent())
    {
        construct.get().setAccessible(true);
        return type.cast(construct.get().newInstance(args));
    }
    else
    {
      throw new NoSuchElementException("Nie znaleziono odpowiedniego konstruktora!");
    }
  }
}
