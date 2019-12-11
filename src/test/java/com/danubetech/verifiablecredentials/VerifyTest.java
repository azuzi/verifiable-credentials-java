package com.danubetech.verifiablecredentials;
import java.util.LinkedHashMap;

import com.github.jsonldjava.utils.JsonUtils;

import info.weboftrust.ldsignatures.verifier.RsaSignature2018LdVerifier;
import junit.framework.TestCase;

public class VerifyTest extends TestCase {

	@SuppressWarnings("unchecked")
	public void testVerify() throws Exception {

		LinkedHashMap<String, Object> jsonLdObject = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(VerifyTest.class.getResourceAsStream("verifiable-credential.ldp.good.jsonld"));
		VerifiableCredential verifiableCredential = VerifiableCredential.fromJsonLdObject(jsonLdObject);

		RsaSignature2018LdVerifier verifier = new RsaSignature2018LdVerifier(TestUtil.testRSAPublicKey);
		boolean verify = verifier.verify(verifiableCredential.getJsonLdObject());

		assertTrue(verify);

		LinkedHashMap<String, Object> jsonLdCredentialSubject = verifiableCredential.getJsonLdCredentialSubject();
		LinkedHashMap<String, Object> jsonLdDriversLicenseObject = (LinkedHashMap<String, Object>) jsonLdCredentialSubject.get("driversLicense");
		String licenseClass = jsonLdDriversLicenseObject == null ? null : (String) jsonLdDriversLicenseObject.get("licenseClass");

		assertEquals("trucks", licenseClass);
	}

	@SuppressWarnings("unchecked")
	public void testBadVerify() throws Exception {

		LinkedHashMap<String, Object> jsonLdObject = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(VerifyTest.class.getResourceAsStream("verifiable-credential.ldp.bad.jsonld"));
		VerifiableCredential verifiableCredential = VerifiableCredential.fromJsonLdObject(jsonLdObject);

		RsaSignature2018LdVerifier verifier = new RsaSignature2018LdVerifier(TestUtil.testRSAPublicKey);
		boolean verify = verifier.verify(verifiableCredential.getJsonLdObject());

		assertFalse(verify);
	}
}