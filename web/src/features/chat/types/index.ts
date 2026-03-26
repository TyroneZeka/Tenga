export interface ChatThread {
  id: string
  listingId: string
  listingTitle: string
  listingImageUrl: string | null
  buyerId: string
  buyerName: string
  sellerId: string
  sellerName: string
  lastMessagePreview: string | null
  unreadCount: number
  updatedAt: string
}

export interface ChatMessage {
  id: string
  threadId: string
  senderId: string
  senderName: string
  content: string
  imageUrl: string | null
  status: 'SENT' | 'DELIVERED' | 'READ'
  createdAt: string
}

export interface MessagePage {
  content: ChatMessage[]
  hasMore: boolean
  nextCursor: string | null
}

export interface SendMessageRequest {
  content: string
  imageUrl?: string
}
