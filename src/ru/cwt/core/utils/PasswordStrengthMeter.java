package ru.cwt.core.utils;

/**
 * Created by d.romanovsky on 14/12/15.
 */

import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.WhitespaceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrengthMeter {

    private static final Logger log = LoggerFactory.getLogger(PasswordStrengthMeter.class);

    private LengthRule lengthRule = new LengthRule(8, 25);
    private WhitespaceRule whitespaceRule = new WhitespaceRule();
    private CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
    private AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();
    private QwertySequenceRule qwertySeqRule = new QwertySequenceRule();
    private RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);
//    private UsernameRule usernameRule = new UsernameRule(true, true);

    private List<Rule> ruleList = new ArrayList<Rule>();
    private PasswordValidator validator;
    private PasswordData passwordData;
    private RuleResult result;

    private PasswordStrengthMeter() {

        charRule.getRules().add(new DigitCharacterRule(3));
        charRule.getRules().add(new NonAlphanumericCharacterRule(1));
        charRule.getRules().add(new UppercaseCharacterRule(1));
        charRule.getRules().add(new LowercaseCharacterRule(1));
        charRule.setNumberOfCharacteristics(2);

        ruleList.add(lengthRule);
//        ruleList.add(usernameRule);
        ruleList.add(whitespaceRule);
        ruleList.add(charRule);
        ruleList.add(alphaSeqRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(repeatRule);

        validator = new PasswordValidator(ruleList);
    }

    public PasswordStrengthMeter(String pass){
        this();
        passwordData = new PasswordData(new Password(pass));
        result = validator.validate(passwordData);
    }

    public static boolean isPasswordSecure(String pass){
        PasswordStrengthMeter psm = new PasswordStrengthMeter(pass);
        return psm.result.isValid();
    }

    public static String getPasswordInfo(String pass){
        PasswordStrengthMeter psm = new PasswordStrengthMeter(pass);
        return psm.result.toString();
    }
}
