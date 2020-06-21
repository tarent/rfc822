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

import java.util.function.BiFunction;

/**
 * Parser base class, abstracting initialisation and movement
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
abstract class Parser {

private static final String BOUNDS_INP = "input is null or size %d exceeds bounds [0;%d]";
private static final String BOUNDS_JMP = "attempt to move (%d) beyond source string (%d)";
private static final String ACCEPT_EOS = "cannot ACCEPT end of input";

/**
 * the {@link String} to analyse
 */
private final String source;
/**
 * offset into {@link #source}
 */
private int ofs;
/**
 * current wide character (wint_t at {@link #source}[{@link #ofs}] or -1 for EOD)
 */
private int cur;
/**
 * offset of next wide character into {@link #source}
 */
private int succ;
/**
 * next wide character (for peeking), cf. {@link #cur}
 */
private int next;
/**
 * source length
 */
private final int srcsz;

/**
 * Constructs a parser. Intended to be used by subclasses only.
 *
 * @param input  user-provided {@link String} to parse
 * @param maxlen subclass-provided maximum input string length, in characters
 *
 * @throws IllegalArgumentException if input was null or too large
 */
Parser(final String input, final int maxlen)
{
	srcsz = input == null ? -1 : input.length();
	if (srcsz < 0 || srcsz > maxlen) {
		source = null; // avoid finaliser attack
		throw new IllegalArgumentException(String.format(BOUNDS_INP,
		    srcsz, maxlen));
	}
	source = input;
	jmp(0);
}

/**
 * Jumps to a specified input character position
 *
 * @param pos to jump to
 *
 * @return the codepoint at that position
 *
 * @throws IndexOutOfBoundsException if pos is not in or just past the input
 */
protected int
jmp(final int pos)
{
	if (pos < 0 || pos > srcsz)
		throw new IndexOutOfBoundsException(String.format(BOUNDS_JMP,
		    pos, srcsz));
	if ((succ = ofs = pos) == srcsz) {
		next = cur = -1;
		return cur;
	}
	cur = source.codePointAt(ofs);
	succ += cur <= 0xFFFF ? 1 : 2;
	next = succ < srcsz ? source.codePointAt(succ) : -1;
	return cur;
}

/**
 * Returns the current input character position, for saving and restoring
 * (with {@link #jmp(int)}) and for error messages
 *
 * @return position
 */
protected int
pos()
{
	return ofs;
}

/**
 * Returns the wide character at the current position
 *
 * @return UCS-4 codepoint, or -1 if end of input is reached
 */
protected int
cur()
{
	return cur;
}

/**
 * Returns the wide character after the one at the current position
 *
 * @return UCS-4 codepoint, or -1 if end of input is reached
 */
protected int
peek()
{
	return next;
}

/**
 * Advances the current position to the next character
 *
 * @return codepoint of the next character, or -1 if end of input is reached
 *
 * @throws IndexOutOfBoundsException if end of input was already reached
 */
protected int
accept()
{
	if (cur == -1)
		throw new IndexOutOfBoundsException(ACCEPT_EOS);
	return jmp(succ);
}

/**
 * Advances the current position as long as the matcher returns true
 * and end of input is not yet reached
 *
 * @param matcher gets called with {@link #cur()} and {@link #peek()} as arguments
 *
 * @return codepoint of the first character where the matcher returned false, or -1
 */
protected int
skip(BiFunction<Integer, Integer, Boolean> matcher)
{
	while (cur != -1 && matcher.apply(cur, next))
		jmp(succ);
	return cur;
}

}