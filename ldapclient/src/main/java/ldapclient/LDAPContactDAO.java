package ldapclient;

import java.util.List;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPContactDAO implements ContactDAO {

	private LdapTemplate ldapTemplate;
    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List getAllContactNames() {
        return ldapTemplate.search("", "(objectclass=person)",
                new ContactAttributeMapper());
    }

	public List getContactDetails(String commonName, String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertContact(ContactDTO contactDTO) {
		Attributes personAttributes = new BasicAttributes();
	    BasicAttribute personBasicAttribute = new BasicAttribute("objectclass");
	    personBasicAttribute.add("person");
	    personAttributes.put(personBasicAttribute);
	    personAttributes.put("cn", contactDTO.getCommonName());
	    personAttributes.put("sn", contactDTO.getLastName());
	    personAttributes.put("description", contactDTO.getDescription());
	    DistinguishedName newContactDN = new DistinguishedName("ou=users");
	    newContactDN.add("cn", contactDTO.getCommonName());
	    ldapTemplate.bind(newContactDN, null, personAttributes);
		
	}

	public void updateContact(ContactDTO contactDTO) {
		Attributes personAttributes = new BasicAttributes();
	    BasicAttribute personBasicAttribute = new BasicAttribute("objectclass");
	    personBasicAttribute.add("person");
	    personAttributes.put(personBasicAttribute);
	    personAttributes.put("cn", contactDTO.getCommonName());
	    personAttributes.put("sn", contactDTO.getLastName());
	    personAttributes.put("description", contactDTO.getDescription());
	    DistinguishedName newContactDN = new DistinguishedName("ou=users");
	    newContactDN.add("cn", contactDTO.getCommonName());
	    ldapTemplate.rebind(newContactDN, null, personAttributes);
		
	}

	public void deleteContact(ContactDTO contactDTO) {
		DistinguishedName newContactDN = new DistinguishedName("ou=users");
	    newContactDN.add("cn", contactDTO.getCommonName());
	    ldapTemplate.unbind(newContactDN);
	}
}
