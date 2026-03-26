import { Outlet, NavLink } from 'react-router-dom'
import { Home, PlusSquare, MessageCircle, User } from 'lucide-react'
import { useThreads } from '@/features/chat'

export default function AppLayout() {
  const { data: threads } = useThreads()
  const totalUnread = threads?.reduce((n, t) => n + t.unreadCount, 0) ?? 0

  return (
    <div className="flex min-h-screen flex-col">
      <div className="flex-1 pb-[56px]">
        <Outlet />
      </div>

      {/* Bottom nav — Taobao-style */}
      <nav className="fixed bottom-0 left-0 right-0 z-30 flex h-14 w-full items-stretch border-t border-gray-200 bg-white shadow-[0_-2px_8px_rgba(0,0,0,0.06)]">
        <NavItem to="/" icon={Home} label="Home" end />
        <NavItem to="/listings/new" icon={PlusSquare} label="Sell" highlight />
        <NavItem to="/chat" icon={MessageCircle} label="Messages" badge={totalUnread} />
        <NavItem to="/profile/me" icon={User} label="Profile" />
      </nav>
    </div>
  )
}

interface NavItemProps {
  to: string
  icon: React.ElementType
  label: string
  badge?: number
  end?: boolean
  highlight?: boolean
}

function NavItem({ to, icon: Icon, label, badge, end, highlight }: NavItemProps) {
  return (
    <NavLink
      to={to}
      end={end}
      className={({ isActive }) =>
        `relative flex flex-1 flex-col items-center justify-center gap-0.5 transition-colors ${
          highlight
            ? 'text-[#ff6000]'
            : isActive
            ? 'text-[#ff6000]'
            : 'text-gray-500 hover:text-[#ff6000]'
        }`
      }
    >
      {({ isActive }) => (
        <>
          {/* Orange top-bar indicator */}
          {(isActive || highlight) && (
            <span className="absolute left-1/2 top-0 h-0.5 w-8 -translate-x-1/2 rounded-b-full bg-[#ff6000]" />
          )}

          <div className="relative">
            <Icon
              className={`h-[22px] w-[22px] ${
                highlight ? 'text-[#ff6000]' : isActive ? 'text-[#ff6000]' : 'text-gray-500'
              }`}
              strokeWidth={isActive || highlight ? 2.2 : 1.8}
            />
            {badge != null && badge > 0 && (
              <span className="absolute -right-2 -top-1.5 flex h-4 min-w-[16px] items-center justify-center rounded-full bg-red-500 px-0.5 text-[9px] font-bold text-white">
                {badge > 99 ? '99+' : badge}
              </span>
            )}
          </div>

          <span className={`text-[10px] font-medium ${isActive || highlight ? 'text-[#ff6000]' : 'text-gray-500'}`}>
            {label}
          </span>
        </>
      )}
    </NavLink>
  )
}
