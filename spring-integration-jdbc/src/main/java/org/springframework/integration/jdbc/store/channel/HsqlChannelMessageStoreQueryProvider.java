/*
 * Copyright 2002-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.jdbc.store.channel;

/**
 * @author Gunnar Hillert
 * @author Artem Bilan
 * @since 2.2
 *
 */
public class HsqlChannelMessageStoreQueryProvider extends AbstractChannelMessageStoreQueryProvider {

	private static final String SELECT_COMMON =
			"SELECT %PREFIX%CHANNEL_MESSAGE.MESSAGE_ID, %PREFIX%CHANNEL_MESSAGE.MESSAGE_BYTES "
					+ "from %PREFIX%CHANNEL_MESSAGE "
					+ "where %PREFIX%CHANNEL_MESSAGE.GROUP_KEY = :group_key and %PREFIX%CHANNEL_MESSAGE.REGION = :region ";

	@Override
	public String getCreateMessageQuery() {
		return "INSERT into %PREFIX%CHANNEL_MESSAGE(MESSAGE_ID, GROUP_KEY, REGION, CREATED_DATE, MESSAGE_PRIORITY, MESSAGE_SEQUENCE, MESSAGE_BYTES)"
				+ " values (?, ?, ?, ?, ?, NEXT VALUE FOR %PREFIX%MESSAGE_SEQ, ?)";
	}

	@Override
	public String getPollFromGroupExcludeIdsQuery() {
		return SELECT_COMMON +
				"and %PREFIX%CHANNEL_MESSAGE.MESSAGE_ID not in (:message_ids) order by CREATED_DATE, MESSAGE_SEQUENCE LIMIT 1";
	}

	@Override
	public String getPollFromGroupQuery() {
		return SELECT_COMMON +
				"order by CREATED_DATE, MESSAGE_SEQUENCE LIMIT 1";
	}

	@Override
	public String getPriorityPollFromGroupExcludeIdsQuery() {
		return SELECT_COMMON +
				"and %PREFIX%CHANNEL_MESSAGE.MESSAGE_ID not in (:message_ids) " +
				"order by MESSAGE_PRIORITY DESC NULLS LAST, CREATED_DATE, MESSAGE_SEQUENCE LIMIT 1";
	}

	@Override
	public String getPriorityPollFromGroupQuery() {
		return SELECT_COMMON +
				"order by MESSAGE_PRIORITY DESC NULLS LAST, CREATED_DATE, MESSAGE_SEQUENCE LIMIT 1";
	}

}
