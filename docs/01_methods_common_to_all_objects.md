# Methods common to all objects

### 10. Obey the general contract when overriding equals

Do not override equals when:
* Each instance of the class is inherently unique
* There is no need for the class to provide a "logical equality" test.
* A superclass has already overridden `equals` and the superclass behavior is appropriate for this class.
* The class is package-private, and you are certain that its equals method will never be invoked.

When you do override the `equals` method, you need to adhere to its general contract:
* *Reflexive* -> for any non-null reference value `x`, `x.equals(x)` must return `true.
* *Symmetric* -> for any non-null reference values `x` and `y`, `x.equals(y)` must return `true` only if `y.equals(x)` returns `true`.
*Transitive* -> for any non-0null reference values `x`, `y`, `z`, if `x.equals(y)` returns `true` and `y.equals(z)` reutrns `true`,
then `x.equals(z)` must return `true`.
* *Consistent* -> for any non-null reference values `x` and `y`, multiple invocations of `x.equals(y)` must consistently return `true`/`false`
provided no information used in `equals` comparisons is modified.
* For any non-null reference value `x`, `x.equals(null)` must return `false`.


### 11. Always override `hashCode when overriding `equals`


### 12. Always override `toString`
When practical, `toString` method should return *all* of the interesting information contained in the object.


### 13. Override `clone` judiciously


### 14. Consider implementing `Comparable`