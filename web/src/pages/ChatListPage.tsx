import { Link, useSearchParams } from 'react-router-dom'
import { useEffect } from 'react'
import { MessageCircle } from 'lucide-react'
import { useThreads, useOpenThread } from '@/features/chat'
import { useAuthStore } from '@/stores/authStore'

export default function ChatListPage() {
  const { data: threads, isLoading } = useThreads()
  const user = useAuthStore((s) => s.user)
  const { mutate: openThread } = useOpenThread()
  const [searchParams] = useSearchParams()
  const listingId = searchParams.get('listingId')
  const sellerId = searchParams.get('sellerId')

  useEffect(() => {
    if (listingId && sellerId) {
      openThread({ listingId, sellerId })
    }
  }, [listingId, sellerId, openThread])

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="mx-auto max-w-xl px-4 py-6">
        <h1 className="mb-4 text-xl font-semibold text-gray-900">Messages</h1>

        {isLoading && (
          <div className="space-y-2">
            {Array.from({ length: 4 }).map((_, i) => (
              <div key={i} className="h-16 animate-pulse rounded-xl bg-gray-200" />
            ))}
          </div>
        )}

        {!isLoading && threads?.length === 0 && (
          <div className="py-16 text-center text-gray-500">
            <MessageCircle className="mx-auto mb-2 h-8 w-8 text-gray-300" />
            <p>No conversations yet.</p>
          </div>
        )}

        {threads && threads.length > 0 && (
          <div className="space-y-1">
            {threads.map((thread) => {
              const other =
                thread.buyerId === user?.id
                  ? { name: thread.sellerName, id: thread.sellerId }
                  : { name: thread.buyerName, id: thread.buyerId }
              return (
                <Link
                  key={thread.id}
                  to={`/chat/${thread.id}`}
                  className="flex items-center gap-3 rounded-xl bg-white p-4 shadow-sm hover:bg-gray-50"
                >
                  <div className="flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-full bg-primary-100 font-medium text-primary-700">
                    {other.name.charAt(0).toUpperCase()}
                  </div>
                  <div className="flex-1 overflow-hidden">
                    <div className="flex items-baseline justify-between">
                      <p className="truncate text-sm font-medium text-gray-900">{other.name}</p>
                      <span className="ml-2 flex-shrink-0 text-xs text-gray-400">
                        {new Date(thread.updatedAt).toLocaleDateString()}
                      </span>
                    </div>
                    <p className="truncate text-xs text-gray-500">
                      {thread.listingTitle}
                      {thread.lastMessagePreview && ` · ${thread.lastMessagePreview}`}
                    </p>
                  </div>
                  {thread.unreadCount > 0 && (
                    <span className="flex h-5 min-w-5 items-center justify-center rounded-full bg-primary-500 px-1 text-xs font-bold text-white">
                      {thread.unreadCount}
                    </span>
                  )}
                </Link>
              )
            })}
          </div>
        )}
      </div>
    </div>
  )
}
