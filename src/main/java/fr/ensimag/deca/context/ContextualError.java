package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;

/**
 * Exception raised when a contextual error is found.
 *
 * @author gl40
 * @date 01/01/2021
 */
public class ContextualError extends LocationException {
    private static final long serialVersionUID = -8122514996569278575L;
    public static final String IDENTIFIER_EXP_UNDEFINED = "Definition in env_exp does not exist with the name :";
    public static final String IDENTIFIER_TYPE_UNDEFINED = "Definition in env_types does not exist with the name :";
    public static final String IDENTIFIER_TYPE_NOTTYPE = "The definition of this identifier is not a type type";
    public static final String DECL_VAR_VOID = "The type of a variable to be declared can not be void";
    public static final String ASSIGN_NOT_COMPATIBLE = "The type of the variable and its assignation are not compatible";
    public static final String EXPR_CONDITION_NOT_BOOLEAN = "The ReturnType of the expression evaluated by the condition is not boolean";
    public static final String PRINT_EXPR_NOT_COMPATIBLE = "The type of expression to be printed is not int, float or string";
    public static final String OP_BINARY_NOT_COMPATIBLE = "At least one of the members of this operation is not compatible with this operator";
    public static final String OP_UNARY_NOT_COMPATIBLE = "The member of this unary operation is not compatible with this operator";
    public static final String LVALUE_IDENT_TYPE = "The nature of the left member is not of type field, parameters of variables";
    public static final String DEFINITION_ALREADY_IN_ENV = "The Definition of this Symbol is already in the environment";
    public static final String CONV_FLOAT_OPERAND_NOT_INT = "The conversion to a float can only be from an int";
    public static final String CAST_INCOMPATIBLE = "This type is not compatible with the casted type";
    public static final String TYPE_NOT_CLASS = "The type of the object to be instantiated is not a class";
    public static final String THIS_NOT_CLASS = "The type of the object pointed by this is not a class";
    public static final String THIS_CLASS_NULL = "The class pointed by this can not be Object class";
    public static final String SELECTION_EXPR_NOT_CLASS = "The type of expression selected is not a class";
    public static final String FIELD_PROTECTED = "You can not have access to this field in this class";
    public static final String SELECTION_CLASS_TYPE_UNDEFINED = "This class is not defined";
    public static final String SELECTION_CLASS_TYPE_NOT_CLASS = "The type of the selected object is not a class";
    public static final String CLASS_NOT_SUBCLASS_PROTECTED = "The selected class is not a subclass of the class where the selection was made";
    public static final String FIELD_NOT_OVERCLASS_PROTECTED = "The class is not a subclass of the one which own this field";
    public static final String RETURN_VOID = "The returnType can not be of type void";


    public ContextualError(String message, Location location) {
        super(message, location);
    }
}
