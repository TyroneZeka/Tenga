/** Matches backend ChatThreadResponse */
export interface ChatThread {
  id: string
  buyerId: string
  sellerId: string
  listingId: string
  lastMessagePreview: string | null
  unreadCount: number
  updatedAt: string
  listingTitle: string | null
  listingFirstImageUrl: string | null
}

/** Matches backend ChatMessageResponse */
export interface ChatMessage {
  id: string
  threadId: string
  senderId: string
  type: 'TEXT' | 'IMAGE'
  body: string
  imageUrl: string | null
  status: 'SENT' | 'DELIVERED' | 'READ'
  sentAt: string
}

/** Matches backend MessageCursorPage */
export interface MessagePage {
  messages: ChatMessage[]
  nextCursor: string | null
  hasMore: boolean
}

export interface SendMessageRequest {
  body: string
  imageUrl?: string
}
