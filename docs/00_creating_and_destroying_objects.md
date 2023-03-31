# Creating and destroying objects


### 1. Consider static factory methods instead of constructors

**Advantages**

* Unlike constructors, the static facttory methods have names
* Unlike constructors, they are not required to create a new object every time they're invoked
* They can return an object of an subtype of their return type
* The class of the returned object can vary from call to call as a function of the imput parameters
* The class of the returned object need not exist when the class containing the moethod is written


**Disadvantages**

* Classes without public or protected constructors cannot be subclassed
* They are sometimes hard for programmers to find. 
Here are some common names for static factory methods:
	* **from** a type conversion method
	* **of** an aggregation method
	* **valueOf** an alternative to *from* and *of*
	* **instance** and **getInstance** - returns an instance that is described by it's parameters (if any)
	* **create** of **newInstance** -  always creates a new instance
	* **get*Type*** - like *getInstance*, but used if the factory method is in a different class
	* **new*Type*** - like *newInstance*, but used if the factory method is in a different class
	* ***type*** - an alternative to *getType* and *newType*
	
	
### 2. Consider a builder when faced with many constructor parameters
Traditionally, programmers used the *telescoping constructor* pattern. With this pattern, it is hard to write client code when there are many parameters, and harder still to read it.
An alternative to this is the *Java beans* pattern, in which you call a parameterless constructor to create the object and then call setter methods to set each required/optional parameter. 
 * a java bean may be in an inconsistent state partway through it's construction
 * the JavaBeans patern precludes the possibility of making a class immutable
 Examples:
 * `NutritionFacts` demonstrates the simple builder pattern
  * `Pizza` demonstrates the usage of builder pattern in class hierarchies (and the covariant return typing).
  
  
### 3. Enforce the singleton propert with a private constructor or an enum type

Making a class a singleton can make it difficult to test its clients because it's impossible to substitute a mock implementation for a singleton unless it implements an interface that serves as its type.
```
//Singleton with public final field
public static class Elvis{
	public static final Elvis INSTANCE = new Elvis();
	private Elvis(){}
	public void leaveThebuilding(){...}
}

//Singleton with static factory
public static class Elvis{
	private static final Elvis INSTANCE = new Elvis();
	private Elvis(){}
	public static final Elvis getInstance() { return INSTANCE; }
	public void leaveThebuilding(){...}
```
Caveat: a privileged client can invoke the private constructor reflectively with the aid of the `AccessibleObject.setAccessible` method. If you need to defent against this attack, modify the constructor to make it throw an exception if it's asked to create a second instance.

To make a singleton class that uses either of these approaches *serializable* it is not sufficient merely to add `implements Serializable`. To maintain the singleton guarantee, declare all instance fields `transient` and provide a `readResolve` method. Otherwise, each time a serialized instance is deserialized, a new instance will be created. 
```
private Object readResolve(){
	return INSTANCE;
}
```
The third approach would be to use an enum singleton:
```
public Enum Elvis {
	INSTANCE;
	public void leaveTheBuilding(){...}
}
```
This approach may feel a bit unnatural, but a single-element enum type is often the best way to implement a singleton. This approach cannot be used if your singleton must extend a superclass other than Enum.


### 4. Enforce noninstantiability with a private constructor

Such classes can be used to group static methods, including factories, for objects that implement some interface. As of java 8, you can also put such methods in the interface.
Attempting to enforce noninstantiability by making a class abstract does not work. The class can be subclassed and the subclass instantiated.
```
public class UtilityClass {
	private UtilityClass() {
		throw new AssertionError();
	}
	....
}
```


### 5. Prefer dependency injection to hardwiring resources

Static utility classes and singletons are inappropriate for classes whose behavior is parametrized by an underlying resource.
A simple patterrn that allows the ability to support multiple instances of a class is to pass the resource into the constructor when creating a new instance. This is one form of *dependency injection*.
```
public class SpellChecker)
	private final Lexicon dictionary;
	
	public SpellChecker(Lexicon dictionary){
		this.dictionary=dictionary;
	}
	public boolean isValid(String word){...}
}
```

### 6. Avoid creating unnecessary objects

**Avoid this**:  `String s=new String("word");`
Another example using regexes:
```
//Performance is very bad if repeatedly reused
	static boolean isRomanNumeral(String s) {
		return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3}$");
	}


//Reusing expensive objects for improved performance	
public class RomanNumerals {
	private static final Patern ROMAN  =Pattern.compile("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3}$")
	
	static boolean isRomanNumeral(String s) {
		return ROMAN.matcher(s).matches();
	}
}
```
Declaring the pattern as a stativ cariable does not only increase performance, but also readability, since it has a name.

Prefer primitives to boxed primitives and watch out for unintentional autoboxing.


### 7. Eliminate obsolete object references - Requires re-visiting


### 8. Avoid finalizers and cleaners - requires revisiting
Finalizers are **deprecated** as of Java 9. The replacement for finalizers is **cleaners**, but still unpredictable, slow, and generally unnecessary.

...

Instead of using finalizers/closers, you can just have your class implement **AutoCloseable** and require its clients to invoke the *close* method on each instance when it is no longer needed.
Closers can still be used as a safety net for classes which implement the AutoCloseable interface (see `Room`).


### 9. Prefer `try-with-resources` to `try-finally`

Why? The code in the finally block may be capable of throwing exceptions.

If you write a class that represents a resource that must be closed, your class should implement `AutoCloseable` too.