package OOP.Solution;

import OOP.Provided.OOP4AmbiguousMethodException;
import OOP.Provided.OOP4MethodInvocationFailedException;
import OOP.Provided.OOP4NoSuchMethodException;
import OOP.Provided.OOP4ObjectInstantiationFailedException;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OOPObject {
    private List<Object> directParents;
    private HashMap<String, Object> virtualAncestor;

    public OOPObject() throws OOP4ObjectInstantiationFailedException {

        directParents = new ArrayList<Object>();
        virtualAncestor = new HashMap<>();

        // get all my annotations - runtime reflection
        Class myself = this.getClass();
        Annotation[] annotations = myself.getAnnotations();

        // for every parent annotation
        // TODO how to create instance of the class using the prent attribute
        // TODO do it recursively DFS
        for (Annotation annotation : annotations) {
            if (annotation instanceof OOPParent) {
                System.out.println(((OOPParent)annotation).parent().getName());
            }

            if (annotation instanceof OOPParents) {
                for (OOPParent parentAnnot : ((OOPParents) annotation).value()) {

                    System.out.println(parentAnnot.parent().getName());
                    Object object = null;
                    try {
                        //parentAnnot.parent().getConstructor().newInstance();
                         object = parentAnnot.parent().getConstructor().newInstance();
                    } catch (Exception e) {
                        throw new OOP4ObjectInstantiationFailedException();
                    }

                    directParents.add(object);
                }
            }
        }
    }

    public boolean multInheritsFrom(Class<?> cls) {
        // get all my annotations - runtime reflection
        // checks only for direct parents now-
        // TODO add recursion
        Class myself = OOPObject.class;
        OOPParent[] annotations = (OOPParent[]) myself.getAnnotationsByType(OOPParent.class);

        for (OOPParent parentAnnot : annotations) {
            Class parent = parentAnnot.parent();
            if (cls.equals(parent)) {
                return true;
            }
        }

        // should we iterate all the parents?
        for (Object obj : directParents) {
            if (obj.getClass().equals(cls)) {
                return true;
            }
            //try ( ((OOPObject)obj).multInheritsFrom(cls)){
        }
        return false;
    }

    public Object definingObject(String methodName, Class<?>... argTypes)
            throws OOP4AmbiguousMethodException, OOP4NoSuchMethodException {
        // TODO: Implement
        //.getMethods()
        //.getDeclaredMethods()

        return null;
    }

    public Object invoke(String methodName, Object... callArgs) throws
            OOP4AmbiguousMethodException, OOP4NoSuchMethodException, OOP4MethodInvocationFailedException {
        // TODO: Implement
        return null;
    }
    public static void main(String[] args) throws OOP4ObjectInstantiationFailedException {

        System.out.print( "A parents: ");
        A a = new A();
        System.out.print( "B parents: ");
        B b = new B();
        System.out.print( "C parents: ");
        C c = new C();
    }
}

class A extends OOPObject {
    public A() throws OOP4ObjectInstantiationFailedException {
        super();
    }
}

@OOPParent(parent = A.class, isVirtual = false)
class B extends OOPObject {
    public B() throws OOP4ObjectInstantiationFailedException {
        super();
    }
}
@OOPParent(parent = A.class, isVirtual = false)
@OOPParent(parent = B.class, isVirtual = false)
class C extends OOPObject {
    public C() throws OOP4ObjectInstantiationFailedException {
        super();
    }
}