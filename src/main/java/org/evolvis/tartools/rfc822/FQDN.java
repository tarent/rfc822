package org.evolvis.tartools.rfc822;

/*-
 * Copyright © 2020 mirabilos (t.glaser@tarent.de)
 * Licensor: tarent solutions GmbH, Bonn
 *
 * Provided that these terms and disclaimer and all copyright notices
 * are retained or reproduced in an accompanying document, permission
 * is granted to deal in this work without restriction, including un‐
 * limited rights to use, publicly perform, distribute, sell, modify,
 * merge, give away, or sublicence.
 *
 * This work is provided “AS IS” and WITHOUT WARRANTY of any kind, to
 * the utmost extent permitted by applicable law, neither express nor
 * implied; without malicious intent or gross negligence. In no event
 * may a licensor, author or contributor be held liable for indirect,
 * direct, other damage, loss, or other issues arising in any way out
 * of dealing in the work, even if advised of the possibility of such
 * damage or existence of a defect, except proven that it results out
 * of said person’s immediate fault when using the work as intended.
 */

/**
 * Represents an FQDN (“domain” production) for use in eMail
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class FQDN extends Parser {

/**
 * Constructs a parser for Fully-Qualified Domain Names
 *
 * @param hostname to parse
 *
 * @throws IllegalArgumentException if input was null or longer than 254 characters
 */
public FQDN(final String hostname)
{
	super(hostname, /* RFC5321 Forward-path limit */ 254);
}

}