import { useEffect } from 'react'
import { Link, useSearchParams, useNavigate } from 'react-router-dom'
import { MessageCircle } from 'lucide-react'
import { useThreads, useOpenThread, useThreadListSocket } from '@/features/chat'
import { useAuthStore } from '@/stores/authStore'

function timeLabel(iso: string) {
  const d = new Date(iso)
  const now = new Date()
  const diffDays = Math.floor((now.getTime() - d.getTime()) / 86_400_000)
  if (diffDays === 0) return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  if (diffDays === 1) return 'Yesterday'
  if (diffDays < 7) return d.toLocaleDateString([], { weekday: 'short' })
  return d.toLocaleDateString([], { month: 'short', day: 'numeric' })
}

export default function ChatListPage() {
  const navigate = useNavigate()
  const { data: threads, isLoading } = useThreads()
  const user = useAuthStore((s) => s.user)
  const { mutate: openThread } = useOpenThread()
  const [searchParams] = useSearchParams()
  const listingId = searchParams.get('listingId')
  const sellerId = searchParams.get('sellerId')

  useThreadListSocket()

  useEffect(() => {
    if (listingId && sellerId) {
      openThread(
        { listingId, sellerId },
        { onSuccess: (thread) => navigate(`/chat/${thread.id}`, { replace: true }) },
      )
    }
  }, [listingId, sellerId, openThread, navigate])

  return (
    <div className="min-h-screen bg-white pb-20">
      {/* Header */}
      <header className="sticky top-0 z-10 border-b border-gray-100 bg-white px-4 py-4">
        <h1 className="text-lg font-bold text-gray-900">Messages</h1>
      </header>

      {/* Loading skeletons */}
      {isLoading && (
        <div className="divide-y divide-gray-100">
          {Array.from({ length: 5 }).map((_, i) => (
            <div key={i} className="flex items-center gap-3 px-4 py-3">
              <div className="h-12 w-12 animate-pulse rounded-full bg-gray-200" />
              <div className="flex-1 space-y-2">
                <div className="h-3.5 w-32 animate-pulse rounded bg-gray-200" />
                <div className="h-3 w-48 animate-pulse rounded bg-gray-100" />
              </div>
              <div className="h-14 w-14 animate-pulse rounded-lg bg-gray-200" />
            </div>
          ))}
        </div>
      )}

      {/* Empty state */}
      {!isLoading && (!threads || threads.length === 0) && (
        <div className="flex flex-col items-center justify-center py-24 text-gray-400">
          <MessageCircle className="mb-3 h-12 w-12 text-gray-200" />
          <p className="text-sm font-medium">No conversations yet</p>
          <p className="mt-1 text-xs text-gray-300">Chat with a seller to get started</p>
        </div>
      )}

      {/* Thread list */}
      {threads && threads.length > 0 && (
        <div className="divide-y divide-gray-100">
          {threads.map((thread) => {
            const isBuyer = thread.buyerId === user?.id
            const otherId = isBuyer ? thread.sellerId : thread.buyerId
            const roleLabel = isBuyer ? 'Seller' : 'Buyer'
            const maskedId = otherId.slice(0, 3) + '***' + otherId.slice(-3)

            return (
              <Link
                key={thread.id}
                to={`/chat/${thread.id}`}
                className="flex items-center gap-3 px-4 py-3 transition-colors hover:bg-gray-50 active:bg-gray-100"
              >
                {/* Avatar with unread badge */}
                <div className="relative flex-shrink-0">
                  <img
                    src={`https://loremflickr.com/48/48/portrait?lock=${otherId.charCodeAt(0) % 50}`}
                    alt={roleLabel}
                    className="h-12 w-12 rounded-full object-cover"
                    onError={(e) => {
                      e.currentTarget.src = `https://ui-avatars.com/api/?name=${roleLabel}&size=48&background=ff6000&color=fff`
                    }}
                  />
                  {thread.unreadCount > 0 && (
                    <span className="absolute -right-0.5 -top-0.5 flex h-4 min-w-[16px] items-center justify-center rounded-full bg-red-500 px-0.5 text-[9px] font-bold text-white">
                      {thread.unreadCount > 99 ? '99+' : thread.unreadCount}
                    </span>
                  )}
                </div>

                {/* Main content */}
                <div className="min-w-0 flex-1">
                  <div className="flex items-baseline justify-between gap-2">
                    <p
                      className={`truncate text-sm font-semibold ${
                        thread.unreadCount > 0 ? 'text-gray-900' : 'text-gray-700'
                      }`}
                    >
                      {thread.listingTitle ?? `${roleLabel} · ${maskedId}`}
                    </p>
                    <span className="flex-shrink-0 text-[11px] text-gray-400">
                      {timeLabel(thread.updatedAt)}
                    </span>
                  </div>
                  <p
                    className={`mt-0.5 truncate text-xs ${
                      thread.unreadCount > 0 ? 'font-medium text-gray-700' : 'text-gray-400'
                    }`}
                  >
                    {thread.lastMessagePreview ?? 'No messages yet'}
                  </p>
                  <p className="mt-0.5 text-[10px] text-gray-300">{maskedId}</p>
                </div>

                {/* Listing thumbnail */}
                {thread.listingFirstImageUrl ? (
                  <img
                    src={thread.listingFirstImageUrl}
                    alt={thread.listingTitle ?? 'Listing'}
                    className="h-14 w-14 flex-shrink-0 rounded-lg object-cover"
                  />
                ) : (
                  <div className="flex h-14 w-14 flex-shrink-0 items-center justify-center rounded-lg bg-gray-100 text-xl">
                    🏷️
                  </div>
                )}
              </Link>
            )
          })}
        </div>
      )}
    </div>
  )
}
