package com.google.gwt.sample.contacts.client;

import java.util.ArrayList;

import com.google.gwt.sample.contacts.shared.Contact;
import com.google.gwt.sample.contacts.shared.ContactDetails;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contactsService")
public interface ContactsService extends RemoteService {

	Contact addContact(Contact contact);

	Boolean deleteContact(String id);

	ArrayList<ContactDetails> deleteContacts(ArrayList<String> ids);

	ArrayList<ContactDetails> getContactDetails();

	Contact getContact(String id);

	Contact updateContact(Contact contact);
}
